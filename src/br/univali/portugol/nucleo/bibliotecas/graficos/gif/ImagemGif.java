/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.univali.portugol.nucleo.bibliotecas.graficos.gif;

import br.univali.portugol.nucleo.bibliotecas.base.ErroExecucaoBiblioteca;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author noschang
 */
public class ImagemGif
{
    public List<QuadroGif> gifFrames;
    private int actualImage;
    public long drawTime = 0;

    public ImagemGif(File arquivo) throws ErroExecucaoBiblioteca
    {
        if (arquivo.exists())
        {
            gifFrames = new ArrayList<>();
            try
            {
                gifFrames.addAll(readGif(arquivo));
            }
            catch (IOException excecao)
            {
                throw new ErroExecucaoBiblioteca(String.format("Ocorreu um erro ao carregar a imagem '%s'", arquivo.getAbsolutePath()));
            }
        }
        else
        {
            throw new ErroExecucaoBiblioteca(String.format("A imagem '%s' não foi encontrada", arquivo.getAbsolutePath()));
        }
    }

    public BufferedImage getActualImage()
    {
        return gifFrames.get(actualImage).imagem;
    }

    public int getActualNumber()
    {
        return actualImage;
    }

    public int getGifDelay()
    {
        return gifFrames.get(actualImage).intervalo;
    }

    public void setActualImage(int actualImage)
    {
        this.actualImage = actualImage;
    }

    public int getFrameNumber()
    {
        return gifFrames.size();
    }

    public void nextImage()
    {
        actualImage = (actualImage + 1) % gifFrames.size();
    }

    private List<QuadroGif> readGif(File stream) throws IOException
    {
        ArrayList<QuadroGif> frames = new ArrayList<>(2);
        ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(ImageIO.createImageInputStream(stream));
        int lastx = 0;
        int lasty = 0;
        int width = -1;
        int height = -1;
        IIOMetadata metadata = reader.getStreamMetadata();
        Color backgroundColor = null;
        if (metadata != null)
        {
            IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
            NodeList globalColorTable = globalRoot.getElementsByTagName("GlobalColorTable");
            NodeList globalScreeDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");
            if (globalScreeDescriptor != null && globalScreeDescriptor.getLength() > 0)
            {
                IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreeDescriptor.item(0);
                if (screenDescriptor != null)
                {
                    width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                    height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                }
            }
            if (globalColorTable != null && globalColorTable.getLength() > 0)
            {
                IIOMetadataNode colorTable = (IIOMetadataNode) globalColorTable.item(0);
                if (colorTable != null)
                {
                    String bgIndex = colorTable.getAttribute("backgroundColorIndex");
                    IIOMetadataNode colorEntry = (IIOMetadataNode) colorTable.getFirstChild();
                    while (colorEntry != null)
                    {
                        if (colorEntry.getAttribute("index").equals(bgIndex))
                        {
                            int red = Integer.parseInt(colorEntry.getAttribute("red"));
                            int green = Integer.parseInt(colorEntry.getAttribute("green"));
                            int blue = Integer.parseInt(colorEntry.getAttribute("blue"));
                            backgroundColor = new Color(red, green, blue);
                            break;
                        }
                        colorEntry = (IIOMetadataNode) colorEntry.getNextSibling();
                    }
                }
            }
        }
        BufferedImage master = null;
        boolean hasBackround = false;
        int frameCount = reader.getNumImages(true);
        for (int frameIndex = 0; frameIndex < frameCount; frameIndex++)
        {
            BufferedImage image;
            try
            {
                image = reader.read(frameIndex);
            }
            catch (IndexOutOfBoundsException io)
            {
                continue;
            }
            if (width == -1 || height == -1)
            {
                width = image.getWidth();
                height = image.getHeight();
            }
            IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            NodeList children = root.getChildNodes();
            int delay = Integer.valueOf(gce.getAttribute("delayTime"));
            String disposal = gce.getAttribute("disposalMethod");
            if (master == null)
            {
                master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                master.createGraphics().setColor(backgroundColor);
                master.createGraphics().fillRect(0, 0, master.getWidth(), master.getHeight());
                hasBackround = image.getWidth() == width && image.getHeight() == height;
                master.createGraphics().drawImage(image, 0, 0, null);
            }
            else
            {
                int x = 0;
                int y = 0;
                for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++)
                {
                    Node nodeItem = children.item(nodeIndex);
                    if (nodeItem.getNodeName().equals("ImageDescriptor"))
                    {
                        NamedNodeMap map = nodeItem.getAttributes();
                        x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
                        y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
                    }
                }
                if (disposal.equals("restoreToPrevious"))
                {
                    BufferedImage from = null;
                    for (int i = frameIndex - 1; i >= 0; i--)
                    {
                        if (!frames.get(i).disposicao.equals("restoreToPrevious") || frameIndex == 0)
                        {
                            from = frames.get(i).imagem;
                            break;
                        }
                    }
                    {
                        ColorModel model = from.getColorModel();
                        boolean alpha = from.isAlphaPremultiplied();
                        WritableRaster raster = from.copyData(null);
                        master = new BufferedImage(model, raster, alpha, null);
                    }
                }
                else if (disposal.equals("restoreToBackgroundColor") && backgroundColor != null)
                {
                    if (!hasBackround || frameIndex > 1)
                    {
                        master.createGraphics().fillRect(lastx, lasty, frames.get(frameIndex - 1).largura, frames.get(frameIndex - 1).altura);
                    }
                }
                master.createGraphics().drawImage(image, x, y, null);
                lastx = x;
                lasty = y;
            }
            try
            {
                BufferedImage copy;
                {
                    ColorModel model = master.getColorModel();
                    boolean alpha = master.isAlphaPremultiplied();
                    WritableRaster raster = master.copyData(null);
                    copy = new BufferedImage(model, raster, alpha, null);
                }
                frames.add(new QuadroGif(copy, delay, disposal, image.getWidth(), image.getHeight()));
            }
            catch (Throwable ex)
            {
                ex.printStackTrace(System.err);
            }
            master.flush();
        }
        reader.dispose();
        return frames;
    }
    
}
