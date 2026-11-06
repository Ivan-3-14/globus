package module1_3;

public class SuppressedDemo {

    public static void main(String[] args) {
        System.out.println("=== Демонстрация suppressed exceptions ===\n");

        try (Connection conn = new Connection("conn-1", false);
             Session session = new Session("sess-1", true);   // close() выбросит исключение!
             File file = new File("file-1", true)) { // close() выбросит исключение!

            // Выбрасываем исключение внутри блока try, чтобы продемонстрировать работу suppressed exceptions
            throw new RuntimeException("BOOM inside try block!");

        } catch (Exception e) {
            System.out.println("\n--- Основное исключение ---");
            System.out.println("Type    : " + e.getClass().getSimpleName());
            System.out.println("Message : " + e.getMessage());

            Throwable[] suppressed = e.getSuppressed();
            System.out.println("\n--- Suppressed (" + suppressed.length + ") ---");
            for (int i = 0; i < suppressed.length; i++) {
                System.out.println("[" + i + "] " + suppressed[i]);
            }
        }
    }
}

/*
 Suppressed exception (подавленное исключение) — это исключение, которое возникло во время обработки другого исключения,
 но было «прикреплено» к основному, чтобы не потеряться. Основное исключение остаётся тем, что вылетает наружу, а
 подавленные доступны через Throwable.getSuppressed().
 Появилось в Java 7 вместе с try-with-resources.

 Пояснение работы на моем примере:

 В блоке try было  выброшено RuntimeException("BOOM inside try block!") — это основное исключение. Началось закрытие
 ресурсов в обратном порядке:
 - File.close() выбросил Exception("File 'file-1' failed to close") → suppressed #0;
 - Session.close() выбросил Exception("Session 'sess-1' failed to close") → suppressed #1;
 - Connection.close() отработал без ошибки.

 RuntimeException не потеряло ошибки закрытия — они прикреплены к нему через getSuppressed(). Поскольку ресурсы закрыва-
 ются в порядке, обратном порядку их открытия, в массиве первым - [0] - идет исключение закрытия File, а за ним - [1] -
 исключение закрытия Session
 */
