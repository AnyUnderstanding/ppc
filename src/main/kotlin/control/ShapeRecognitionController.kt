package control

import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.factory.Nd4j
import java.io.File


class ShapeRecognitionController {
    private lateinit var model: MultiLayerNetwork

    init {
        loadModel()
    }

    fun loadModel(){
        val SIMPLE_MLP: String = File("saved_model-0.950.h5").getAbsolutePath()

        // Keras Sequential models correspond to DL4J MultiLayerNetworks. We enforce loading the training configuration
        // of the model as well. If you're only interested in inference, you can safely set this to 'false'.

        // Keras Sequential models correspond to DL4J MultiLayerNetworks. We enforce loading the training configuration
        // of the model as well. If you're only interested in inference, you can safely set this to 'false'.
        model = KerasModelImport.importKerasSequentialModelAndWeights(SIMPLE_MLP, false)
    }

    fun predict(){
        // Test basic inference on the model.
        val input = Nd4j.create(256, 100)
        val output = model.output(input)

        // Test basic model training.

        // Test basic model training.
        model.fit(input, output)
    }
}