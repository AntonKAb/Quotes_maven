public class DateError extends Exception{
    public String toString(){
        return "\nОшибка ввода: неверно записана дата\nНеобходимый формат даты: dd/MM/yyyy\n";
    }
}

