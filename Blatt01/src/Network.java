import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Network class implements a neural network.
 * The network consists of three types of neurons: photoreceptors(@see
 * Photoreceptor), interneurons(@see Interneuron) and cortical neurons(@see
 * CorticalNeuron). The network processes light waves. There are three types of
 * photoreceptors, that perceive the different colors.
 * 
 * @author Vera Röhr
 * @version 1.0
 * @since 2019-01-11
 */
public class Network {
	/** #Photoreceptors in the network */
	int receptors;
	/** #Cortical neurons in the network */
	int cortical;
	/** All the neurons in the network */
	Neuron[] neurons;
	/** Different receptor types */
	String[] receptortypes = { "blue", "green", "red" };

	/**
	 * Adds neurons to the network.
	 * <p>
	 * Initializes the neurons in the network. Should initialize approximately equal numbers
     * of the different types of photoreceptors. Synapses are not added here! 
	 * 
	 * @param inter
	 *            #Interneurons
	 * @param receptors
	 *            #Photoreceptors
	 * @param cortical
	 *            #CorticalNeurons
	 * @throws RuntimeException
     *            if there are less than 3 photoreceptors or less interreceptors than photoreceptors.            
	 */
	
	public Network(int inter, int receptors, int cortical) {
		if (receptors < 3 || inter < receptors ) { throw new RuntimeException(); }
		else {
			this.receptors = receptors;
			this.cortical = cortical;
			int index;
			for(index = 0; index < receptors; index++ ) {
				if (index % 3 == 0) { neurons[index] = new Photoreceptor(index, receptortypes[0]); }
				else if (index % 3 == 1) { neurons[index] = new Photoreceptor(index, receptortypes[1]); }
				else if (index % 3 == 2) { neurons[index] = new Photoreceptor(index, receptortypes[2]); }
			}
			int indexOfLastReceptor = index;

		}
	}

	/**
	 * Add a Synapse between the Neurons. The different neurons have their outgoing
	 * synapses as an attribute and each a method addSynapse. ({@link Interneuron}, {@link Photoreceptor},
	 * {@link CorticalNeuron})
	 * 
	 * @param n1
	 *            Presynaptic Neuron (Sender)
	 * @param n2
	 *            Postsynaptic Neuron (Receiver)
	 */

	public void addSynapse(Neuron n1, Neuron n2) {
		new Synapse(n1, n2);
	}
	/**
	 * Processes the light waves. The lightwaves are integrated by the
	 * photoreceptors (@see Photoreceptor.integrateSignal(double[] signal)) and the
	 * final neural signal is found by summing up the signals in the cortical
	 * neurons(@see CorticalNeuron)
	 * 
	 * @param input
	 *            light waves in nm
	 * @return the neural signal that can be used to classify the color
	 */
	// changed double[] to int in function head and return 0 instead of return signal
		public int signalprocessing(double[] input) {
			double[] signal;
			// TODO
			return 0;
		}

	public double[] countColorreceptors() {
		double[] colorreceptors = new double[3];
		Photoreceptor c;
		for (Neuron neuron : this.neurons) {
			if (neuron instanceof Photoreceptor) {
				c = (Photoreceptor) neuron;
				if (c.type.equals("blue"))
					colorreceptors[0]++;
				else if (c.type.equals("green"))
					colorreceptors[1]++;
				else if (c.type.equals("red"))
					colorreceptors[2]++;
			}
		}
		return colorreceptors;
	}

	/**
	 * Classifies the neural signal to a color.
	 * 
	 * @param signal
	 *            neural signal from the cortical neurons
	 * @return color of the mixed light signals as a String
	 */
	public String colors(double[] signal) {
        String color = "grey";
		if (signal[0] > 0.6 && signal[1] < 0.074)
			color = "violet";
		else if (signal[0] >= 0.21569329706627882 && signal[1] < 0.678)
			color = "blue";
		else if (signal[0] < 0.21569329706627882 && signal[1] >= 0.678 && signal[2] > 0.333)
			color = "green";
		else if (signal[1] < 0.713 && signal[2] > 0.913)
			color = "yellow";
		else if (signal[1] > 0.068 && signal[2] > 0.227)
			color = "orange";
		else if (signal[2] > 0.002)
			color = "red";
		return color;
	}

	public static void main(String[] args) {
    /*
		//This is a example implementation of a main. Start with the simplest network possible as it is shown in the video.
		Network neural = new Network(3, 3, 1);
        //use neural.addSynapse to add the synapses based on the order of neurons in the neurons array. 
        //For the structure of the network look at the video
        
        
		double coloredLight[] = {478} ;
		System.out.println(neural.colors(neural.signalprocessing(coloredLight)));
		CorticalNeuron n= (CorticalNeuron) neural.neurons[6];
		n.reset();
		coloredLight[0]= 578;
		System.out.println(neural.colors(neural.signalprocessing(coloredLight)));
	*/
	}

}


