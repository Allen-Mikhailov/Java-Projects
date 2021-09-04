import java.util.Random;

public class Network
{
   static float e = 2.71828f;
   
   private int HiddenNodes;
   private int HiddenLayers;
   private int InputCount;
   private int OutputCount;
   
   
   private float[][][] HiddenWeights;
   private float[][] HiddenBiases;
   private float[][] OutputWeights;
   private float[] OutputBiases;
   
   private float Sigmoid(float x)
   {
      return 1f/(float) (1f+Math.pow(e, -x));
   }
   
   public Network(int hl, int ln, int in, int on)
   {
      Random rand = new Random();
   
      //Setting Variables
      HiddenLayers = hl;
      HiddenNodes = ln;
      InputCount = in;
      OutputCount = on;
      
      //Creating Layers
      for (int i = 0; i < HiddenLayers; i++)
      {
         for (int j = 0; j < HiddenNodes; j++)
         {
            HiddenBiases[i][j] = (rand.nextFloat()-.5f)*2f;
            for (int k = 0; k < HiddenNodes; k++)
            {
               HiddenWeights[i][j][k] = (rand.nextFloat()-.5f)*2f;
            }
         }
      }
      
      //Creating Outputs
       for (int i = 0; i < OutputCount; i++)
      {
         OutputBiases[i] = (rand.nextFloat()-.5f)*2f;
         for (int j = 0; j < HiddenNodes; j++)
         {
            OutputWeights[i][j] = (rand.nextFloat()-.5f)*2f;
         }
      }
   }
   
   public float[] Run(float[] Inputs)
   {
      float[] Output = new float[HiddenNodes];
      
      //Input
      for (int i = 0; i < HiddenNodes; i++)
      {
         float out = HiddenBiases[0][i];
         for (int j = 0; j < InputCount; j++)
         {
            out += HiddenWeights[0][i][j]*Inputs[j];
         }
         Output[i] = Sigmoid(out);
      }
      
      //Hidden Layers
      for (int i = 1; i < HiddenLayers; i++)
      {
         for (int j = 0; j < HiddenNodes; j++)
         {
            float Out = HiddenBiases[i][j];
            for (int k = 0; k < HiddenNodes; k++)
            {
               Out += HiddenWeights[i][j][k]*Output[k];
            }
            Output[j] = Sigmoid(Out);
         }
      }
      
      //Output Layer
      float[] Final = new float[OutputCount];
         for (int i = 0; i < OutputCount; i++)
      {
         float out = OutputBiases[i];
         for (int j = 0; j < HiddenNodes; j++)
         {
            out += OutputWeights[i][j]*Inputs[j];
         }
         Final[i] = Sigmoid(out);
      }
      
         return Final;
   }
   
   public void Save(String filename)
   {
      
   }
   
   public int[] GetBuild()
   {
      return new int[] { HiddenLayers, HiddenNodes, InputCount, OutputCount };
   }
}
