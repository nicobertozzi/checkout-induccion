import routes.RestEndpoint;

public class Main {

    public static void main(final String[] args) throws Exception {
        new RestEndpoint().init();

        System.out.println(" >>>> Server is running...");
    }

}
