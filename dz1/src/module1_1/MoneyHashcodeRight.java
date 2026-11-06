package module1_1;

import java.math.BigDecimal;
import java.util.Objects;

public class MoneyHashcodeRight {
    private final Currency currency;
    private final BigDecimal amount;

    public MoneyHashcodeRight(Currency currency, BigDecimal amount) {
        this.currency = currency;
        this.amount = amount;
    }

    /*
    Сравниваем amount через compareTo(), чтобы избежать ситуации, при которой 100.00 и 100.0 будут считаться разными
    значениями(как, например, при сравнении через Objects.eaquals())
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoneyHashcodeRight money = (MoneyHashcodeRight) o;

        return currency == money.currency &&
                amount.compareTo(money.amount) == 0;
    }

    /*
    Метод stripTrailingZeros() удаляет незначащие нули в конце (справа от запятой), уменьшая scale у BigDecimal.
    Делается это для того,чтобы избежать ситуации, при которой 100.00 и 100.0 будут считаться разными значениями
     */
    @Override
    public int hashCode() {
        return Objects.hash(currency, amount.stripTrailingZeros());
    }
}
