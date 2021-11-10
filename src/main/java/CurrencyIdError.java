public class CurrencyIdError extends Exception{
    public String toString(){
        return "\nОшибка ввода: неверно записан идентификатор валюты\nПример правильного ввода: USD, EUR\n";
    }
}