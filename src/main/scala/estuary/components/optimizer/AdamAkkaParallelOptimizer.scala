package estuary.components.optimizer

import breeze.linalg.DenseMatrix
import estuary.components.optimizer.AdamOptimizer.AdamParam

class AdamAkkaParallelOptimizer(override val iteration: Int,
                                override val learningRate: Double,
                                override val paramSavePath: String,
                                override val miniBatchSize: Int,
                                override val momentumRate: Double,
                                override val adamRate: Double)
  extends AdamOptimizer(iteration, learningRate, paramSavePath, miniBatchSize, momentumRate, adamRate)
    with CentralizedAkkaParallelOptimizer[AdamParam, Seq[DenseMatrix[Double]]] {

  /**
    * Given model parameters to initialize optimization parameters, i.e. for Adam Optimization, model parameters are of type
    * "Seq[DenseMatrix[Double] ]", optimization parameters are of type "AdamParams", i.e. case class of
    * (Seq[DenseMatrix[Double] ], Seq[DenseMatrix[Double] ], Seq[DenseMatrix[Double] ])
    *
    * @param modelParams model parameters
    * @return optimization parameters
    */
  protected def modelParamsToOpParams(modelParams: Seq[DenseMatrix[Double]]) = AdamParam(modelParams, getInitAdam(modelParams), getInitAdam(modelParams))

  protected def opParamsToModelParams(opParams: AdamParam): Seq[DenseMatrix[Double]] = opParams.modelParam
}

object AdamAkkaParallelOptimizer {
  def apply(iteration: Int = 100, learningRate: Double = 0.001, paramSavePath: String = System.getProperty("user.dir"), miniBatchSize: Int = 64, momentumRate: Double = 0.9, adamRate: Double = 0.999): AdamAkkaParallelOptimizer = {
    new AdamAkkaParallelOptimizer(iteration, learningRate, paramSavePath, miniBatchSize, momentumRate, adamRate)
  }
}


