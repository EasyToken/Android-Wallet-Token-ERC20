package info.bcdev.easytoken;

public class config {

    public static String addressethnode(int node) {
        switch(node){
            case 1:
                return "http://176.74.13.102:18087";
            case 2:
                return "http://192.168.0.33:8547";
            default:
                    return "https://mainnet.infura.io/avyPSzkHujVHtFtf8xwY";
        }
    }

    public static String addresssmartcontract(int contract) {
        switch (contract){
            case 1:
                return "0x5C456316Da36c1c769FA277cE677CB8F690c5767";
            default :
                return "0x89205A3A3b2A69De6Dbf7f01ED13B2108B2c43e7";
        }
    }

    public static String passwordwallet() {
        return "";
    }


}
