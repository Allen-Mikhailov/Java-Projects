import java.util.Random;

public class Runner
{
   public static void Display(int[] array)
   {
      String Output = "";
      for (int i = 0; i < array.length; i++)
      {
         Output += Integer.toString(array[i]) + ", ";
      }
      System.out.println(Output);
   }

   public static int[] RunOnImage(int[] Image, Network network)
   {
      try {
         //Converting To floats and taking out width and height values
         float[] newScaledData = new float[Image.length-2];
         for (int i = 2; i < Image.length; i++)
         {
            newScaledData[i-2] = Image[i] / 255;
         }
         
         float[] UnCompressedData = network.Run(newScaledData);

         //Converting to ints
         int[] newImage = new int[UnCompressedData.length+2];
         newImage[0] = Image[0];
         newImage[1] = Image[1];
         
         for (int i = 0; i < UnCompressedData.length; i++)
         {
            newImage[i+2] = (int) Math.floor( (double) UnCompressedData[i] * 255);
         }

         return newImage;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return new int[] {0, 0};
   }

   public static void main(String[] args)
   {
      try {
         int Generations = 1000;
         int NetworksPerGen = 2;

         int ImageSize = 25;

         float changeWeight = .01f;

         int Layers = 5;
         int NodesPerLayer = ImageSize*ImageSize * 3;
         int Inputs = ImageSize*ImageSize * 3;
         int Outputs = (int) Math.pow(ImageSize*2, 2) * 3;

         int[][] ImageSamples = new int[][] {
            ImageHandler.Read("Samples/Sample1.png"),
            ImageHandler.Read("Samples/Sample2.png"),
            ImageHandler.Read("Samples/Sample3.jfif")
         };

         int[][] ScaledSamples = new int[][] {
            ImageHandler.ScaleDown(ImageSamples[0]),
            ImageHandler.ScaleDown(ImageSamples[1]),
            ImageHandler.ScaleDown(ImageSamples[2])
         };

         Network[] networks = new Network[NetworksPerGen];

         double TotalScore = 0;

         //Creating Initial Networks 

         for (int i = 0; i < NetworksPerGen; i++)
         {
            networks[i] = new Network(Layers, NodesPerLayer, Inputs, Outputs);
         }

         for (int i = 0; i < Generations; i++)
         {
            int SampleIndex = (int) (Math.random()*ImageSamples.length);

            int[] Image = ImageSamples[SampleIndex];
            int[] ScaledImage = ScaledSamples[SampleIndex];

            double BestScore = 0;
            int[] BestImage = {};

            double[] TopScores = new double [NetworksPerGen/2];
            int[] TopNetworks = new int [NetworksPerGen/2];

            //Filling top scores
            for (int j = 0; j < TopScores.length; j++)
               TopScores[j] = 0;
            
            //Running Networks
            for (int j = 0; j < NetworksPerGen; j++)
            {
               int[] newImage = RunOnImage(ScaledImage, networks[j]);
               double score = ImageHandler.Compare(Image, newImage);

               //Looking for place to insert
               for (int k = 0; k < TopScores.length; k++)
               {
                  if (score > TopScores[k])
                  {
                     int PointerNetwork = j;
                     double PointerScore = score;

                     for (int l = k; l < TopScores.length; l++)
                     {
                        int newNetworkId = TopNetworks[l];
                        double newScore = TopScores[l];
                        
                        TopNetworks[l] = PointerNetwork;
                        TopScores[l] = PointerScore;

                        PointerNetwork = newNetworkId;
                        PointerScore = newScore;
                     }

                     break;
                  }
               }

               TotalScore += score;

               //Only used for best Image
               if (score > BestScore)
               {
                  BestScore = score;
                  BestImage = newImage;
               }
            }

            //Writing Best Image to file
            ImageHandler.Write("BestGeneration/Generation" + Integer.toString(i) + ".jpg", BestImage);

            //Creating New Generation
            Network[] newNetworks = new Network[NetworksPerGen];
            for (int j = 0; j < TopNetworks.length; j++)
            {
               newNetworks[j*2] = networks[TopNetworks[j]];
               newNetworks[j*2 + 1] = new Network(newNetworks[j*2], changeWeight);
            }

            networks = newNetworks;

            System.out.println("Generation : " + Integer.toString(i) + ", Best Score : " + Double.toString(BestScore) 
               + ", Sample : " + Integer.toString(SampleIndex) 
               + ", Average Score : " + Double.toString(TotalScore / ((i+1) * NetworksPerGen)));
         }
         
         System.out.println("The Simulation has Finished");
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}