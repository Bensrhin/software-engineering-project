public class ChebychevValues {

    public float ChebyValuesCos(int j, int k){
        if (j == 1){
            return 0.8775825618903728f;
        }
        else if (j == 2) {
            return 0.9689124217106447f;
        }
        else if (j == 3) {
            return 0.992197667229329f;
        }
        else {
            return 0.9980475107000991f;
        }
    }

    public float ChebyValuesSin(int j, int k){
        if (j == 1){
            return 0.479425538604203f;
        }
        else if (j == 2) {
            return 0.24740395925452294f;
        }
        else if (j == 3) {
            return 0.12467473338522769f;
        }
        else {
            return 0.0624593178423802f;
        }
    }

    public float ChebyValuesActan(int i){
        if (i == 0) {
            return -0.09849139859277231f;
        }
        if (i == 1) {
            return -2.909727065921128f;
        }
        if (i == 2) {
            return 0.9508941577113765f;
        }
        if (i == 3) {
            return -0.4009904007516215f;
        }
        if (i == 4) {
            return -20.88540204971548f;
        }
        if (i == 5) {
            return 0.5168632143106962f;
        }
        if (i == 6) {
            return -0.7834144432251993f;
        }
        if (i == 7) {
            return 4.170396957285327f;
        }
        if (i == 8) {
            return 0.1963273487588419f;
        }
        else {
            return 0;
        }

    }

}
