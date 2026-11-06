package module1_1;

import java.math.BigDecimal;

public class MoneyHashcodeWrong {
    private final Currency currency;
    private final BigDecimal amount;

    public MoneyHashcodeWrong(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoneyHashcodeWrong money = (MoneyHashcodeWrong) o;

        return currency == money.currency && amount.compareTo(money.amount) == 0;
    }

    // метод хэшкод  переопределен неправильно - поэтому в классе MoneyDemo в первом выводе демонстрируется, что объекты
    // равны по equals, но в HashSet добавлены оба элемента, поэтому (второй вывод) - его размер 2.
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
}
