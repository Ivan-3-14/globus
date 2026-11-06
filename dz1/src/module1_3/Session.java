package module1_3;

public class Session implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;

    public Session(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        System.out.println("Open Session '" + name + "'");
    }

    public void execute(String query) {
        System.out.println("Use Session '" + name + "' executes: " + query);
    }

    @Override
    public void close() throws Exception {
        System.out.println("Close Session '" + name + "'");
        if (failOnClose) {
            throw new Exception("Session '" + name + "' failed to close");
        }
    }
}
