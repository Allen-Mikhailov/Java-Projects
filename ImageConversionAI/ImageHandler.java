import java.io.File;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImageHandler
{
   public static int[] Read(String filename) throws Exception
   {
      File image = new File(filename);
      BufferedImage img = ImageIO.read(image);
      
      int Width = img.getHeight();
      int Height = img.getWidth();
      
      Height -= Height % 2;
      Width -= Width % 2;
      
      int[] Data = new int[2+ Height*Width];
      
      Data[0] = Height;
      Data[1] = Width;
      
      for (int y = 0; y < Height; y++) {
         for (int x = 0; x < Width; x++) {
         
            //Retrieving contents of a pixel
            int pixel = img.getRGB(x,y);
            
            int Index = 2 + (y * Height + x);
            
            Data[Index] = pixel;
         }
      }
      
      return Data;
   }
   
   public static int[] ScaleDown(int[] Image)
   {
      int ImageHeight = Image[0];
      int ImageWidth = Image[1];
      
      int newHeight = ImageHeight / 2;
      int newWidth = ImageWidth / 2;
      
      int[] newImage = new int[2 + (newHeight * newWidth)];
      newImage[0] = newHeight;
      newImage[1] = newWidth;
      
      for (int i = 0; i < newHeight; i++)
      {
         for (int j = 0; j < newWidth; j++)
         {
            int Index = 2 + (i * newHeight * 4 + j*2);

            Color TopLeft = new Color(Image[Index], true);
            Color TopRight = Image[Index + 1];
            Color BottomLeft = Image[Index + newHeight * 2];
            Color BottomRight = Image[Index + newHeight * 2 + 1];
            
            int Red = (Image[Index] + Image[Right] + Image[Bottom] + Image[BottomRight]) / 4;
            int Blue = (Image[Index+1] + Image[Right+1] + Image[Bottom+1] + Image[BottomRight+1]) / 4;
            int Green = (Image[Index+2] + Image[Right+2] + Image[Bottom+2] + Image[BottomRight+2]) / 4;
            
            int newIndex = 2 + (i * newHeight + j)*3;
            newImage[newIndex] = new Color(Red, Blue, Green).getRGB();
         }
      }
      
      return newImage;
   }

   public static double Compare(int[] Image1, int[] Image2)
   {
      //Remember to Compensate for the Height and Width Values
      double Weight = 1 / (double) ((Image1.length-2) * 255);

      double score = 1;

      for (int i = 2; i < Image1.length; i++)
      {
         score -= Math.abs(Image1[i] - Image2[i]) * Weight;
      } 

      return score;
   }
   
   public static void Write(String FileName, int[] ImageData)
   {
      
      int Height = ImageData[0];
      int Width = ImageData[1];
      
      BufferedImage image = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
      Graphics g = image.createGraphics();
      
      g.setColor(Color.WHITE);
      g.fillRect(0 , 0,  Width , Height);
      
      //Looping through all Pixels
      for (int y = 0; y < Height; y++) {
         for (int x = 0; x < Width; x++) {
            int DataIndex = 2 + (y * Height + x)*3;
            g.setColor(new Color(ImageData[DataIndex], ImageData[DataIndex+1], ImageData[DataIndex+2], 255));
            g.fillRect(x, y, 1, 1);
         }
      }
     
      try {
         ImageIO.write(image, "jpg", new File(FileName));
      } catch (IOException e) { 
         e.printStackTrace();
      }
   }
}