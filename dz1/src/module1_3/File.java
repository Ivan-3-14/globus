package module1_3;

public class File implements AutoCloseable {

    private final String name;
    private final boolean failOnClose;

    public File(String name, boolean failOnClose) {
        this.name = name;
        this.failOnClose = failOnClose;
        System.out.println("Open File '" + name + "'");
    }

    public void write(String data) {
        System.out.println("Use File '" + name + "' writes: " + data);
    }

    @Override
    public void close() throws Exception {
        System.out.println("Close File '" + name + "'");
        if (failOnClose) {
            throw new Exception("File '" + name + "' failed to close");
        }
    }
}
