package module1_3;

public class Connection implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;

    public Connection(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        System.out.println("Open Connection '" + name + "'");
    }

    public void send(String data) {
        System.out.println("Use Connection '" + name + "' sends: " + data);
    }

    @Override
    public void close() throws Exception {
        System.out.println("Close Connection '" + name + "'");
        if (failOnClose) {
            throw new Exception("Connection '" + name + "' failed to close");
        }
    }
}
