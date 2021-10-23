package testq;

public class zi extends fu {
    public void info(){
        System.out.println("我是一个苹果总"+weight+"g!");
    }
       
    public static void main(String[] args) {
        zi i = new zi();

        i.weight = 56;
        i.info();
    }
}
