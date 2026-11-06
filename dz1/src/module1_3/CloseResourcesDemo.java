package module1_3;

public class CloseResourcesDemo {

    public static void main(String[] args) {
        System.out.println("=== Демонстрация порядка закрытия ресурсов ===\n");

        try (Connection conn = new Connection("conn-1", false);
             Session session = new Session("sess-1", false);
             File file = new File("file-1", false)) {

            conn.send("hello");
            session.execute("SELECT 1");
            file.write("data");

        } catch (Exception e) {
            System.out.println("Caught: " + e);
        }

        System.out.println("\nВывод: ресурсы закрываются в порядке, обратном порядку их объявления.");
    }
}
