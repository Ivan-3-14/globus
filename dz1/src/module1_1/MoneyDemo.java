package module1_1;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class MoneyDemo {
    public static void main(String[] args) {

        /*
        Сначала демонстрируется неправильное переопределение хэшкода. В HashSet добавляются два абсолютно идентичных
        значения. Затем они сравниваются между собой по equals, чтобы показать их идентичность, но при этом размер
        HashSet'а - 2, что означает, что оба элемента были добавлены в него.
         */
        Set<MoneyHashcodeWrong> wrongSet = new HashSet<>();
        MoneyHashcodeWrong moneyWrong1 = new MoneyHashcodeWrong(
                Currency.RUB,
                new BigDecimal("100.00")
        );
        wrongSet.add(moneyWrong1);
        MoneyHashcodeWrong moneyWrong2 = new MoneyHashcodeWrong(
                Currency.RUB,
                new BigDecimal("100.00")
        );
        wrongSet.add(moneyWrong2);
        System.out.println("If first element equals second element: " + moneyWrong1.equals(moneyWrong2));
        System.out.println("Wrong HashSet have " + wrongSet.size() + " elements: ");

        /*
        Затем демонстрируется, как должно быть при правильном переопределении хэшкода. Точно так же добавляются два
        идентичных значения в HashSet, и выводится его размер - 1.
         */
                Set<MoneyHashcodeRight> rightSet = new HashSet<>();
        MoneyHashcodeRight rightMoney1 =  new MoneyHashcodeRight(
                Currency.RUB,
                new BigDecimal("100.00")
        );
        rightSet.add(rightMoney1);
        MoneyHashcodeRight rightMoney2 =  new MoneyHashcodeRight(
                Currency.RUB,
                new BigDecimal("100.0")
        );
        rightSet.add(rightMoney2);
        System.out.println("Right HashSet have " + rightSet.size() + " elements: ");
    }
}
