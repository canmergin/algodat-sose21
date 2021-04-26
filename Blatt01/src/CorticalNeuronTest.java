import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorticalNeuronTest {

    @Test
    void constructor () {
        for(int i = 0; i < 10; i++){
            CorticalNeuron n = new CorticalNeuron(i);
            assertEquals(n.index, i);
        }
    }
}