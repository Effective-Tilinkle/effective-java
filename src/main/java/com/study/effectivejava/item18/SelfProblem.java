package com.study.effectivejava.item18;

public class SelfProblem {
    interface SomethingWithCallback {

        void doSomething();

        void call();

    }


    /*
    target클래스가 나 자신을 넘겨서 사용하는 메서드가 있고, 콜백이나 다른 이유로 넘겨받은쪽에서 target 클래스의 메서드를 호출할때(ex. visitor패턴),
    이를 wrapper 클래스로 만든다면 기대하는 바와 다르게 동작할 수 있다.
    즉, wrapper 클래스의 메서드가 호출되지않고 target 클래스가 호출된다..
    왜냐하면, 참조(target클래스)를 넘겨받은 클래스는 wrapper클래스를 알 길이 없다..
    그래서 조슈아블로크는 콜백과 어울리지않는다고 함..

     */
    static class WrappedObject implements SomethingWithCallback {

        private final SomeService service;

        WrappedObject(SomeService service) {
            this.service = service;
        }

        @Override
        public void doSomething() {
            service.performAsync(this);
        }

        @Override
        public void call() {
            System.out.println("WrappedObject callback!");
        }
    }


    static class Wrapper implements SomethingWithCallback {

        private final WrappedObject wrappedObject;

        Wrapper(WrappedObject wrappedObject) {
            this.wrappedObject = wrappedObject;
        }

        @Override
        public void doSomething() {
            wrappedObject.doSomething();
        }

        void doSomethingElse() {
            System.out.println("We can do everything the wrapped object can, and more!");
        }

        @Override
        public void call() {
            System.out.println("Wrapper callback!");
        }
    }

    static final class SomeService {

        void performAsync(SomethingWithCallback callback) {
            new Thread(() -> {
                perform();
                callback.call();
            }).start();
        }

        void perform() {
            System.out.println("Service is being performed.");
        }
    }
    public static void main(String[] args) {
        SomeService   service       = new SomeService();
        WrappedObject wrappedObject = new WrappedObject(service);
        Wrapper       wrapper       = new Wrapper(wrappedObject);
        wrapper.doSomething();
    }
}
