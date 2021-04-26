import java.util.ArrayList;

/**
 * The class Neuron implents a interneuron for the class Network.
 * 
 * @author Vera Röhr
 * @version 1.0
 * @since 2019-01-11
 */
public class Interneuron extends Neuron {
	/**
	 * {@inheritDoc}
	 */
	public Interneuron(int index) {
		super(index);
		this.outgoingsynapses = new ArrayList<Synapse>();

	}

	/**
	 * Divides incoming signal into equal parts for all the outgoing synapses
	 * Transmits the parts via Synapse.transmit(Double[]);
	 * 
	 * @param signal 3 dimensional signal from another neuron
	 * @return 3 dimensional neural signal, which got transmitted to the synapses (for testing.)
	 */
	@Override
	public double[] integrateSignal(double[] signal) {
		for (int i = 0; i < outgoingsynapses.size(); i++) {
			this.outgoingsynapses.get(i).transmit(signal);
		}
		return signal;
	}
}


