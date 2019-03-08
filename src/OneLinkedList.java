class OneLinkedList<E>{
    private int size;
    private Node<E> first;

    private class Node<T>{
        T item;
        Node<T> next;

        Node(T element){
            this.item = element;
            this.next = null;
        }

        Node(Node<T> next, T element) {
            this.item = element;
            this.next = next;
        }

        Node<T> getNext(){
            return this.next;
        }

        void setNext(Node<T> next){
            this.next = next;
        }

        T getItem() {
            return this.item;
        }
    }

    OneLinkedList(){
        size = 0;
    }

    OneLinkedList(E item){
        size = 1;
        first = new Node<>(item);
    }

    OneLinkedList<E> revert(){
        OneLinkedList<E> newList = new OneLinkedList<>();
        for(int i = this.getSize() - 1; i >= 0; i--){
            newList.add(this.get(i));
        }
        return newList;
    }

    void show(){
        for(int i = 0; i < this.getSize(); i++){
            System.out.print(this.get(i) + ", ");
        }
        System.out.println();
    }

    E get(int index) throws OutOfSizeException{
        if(index == 0){
            return first.getItem();
        }else if(index <= size){
            Node<E> nextElement = first.getNext();
            for (int i = 2; i <= index; i++){
                nextElement = nextElement.getNext();
            }
            return nextElement.getItem();
        }else{
            throw new OutOfSizeException("Unable to get element on the index " + index + ".");
        }
    }

    void add(E e) {
        if(size == 0){
            first = new Node<>(e);
            size++;
        }else {
            if (first.getNext() != null) {
                Node<E> nextElement = first.getNext();
                Node<E> element;
                while (true) {
                    if (nextElement.getNext() != null) {
                        nextElement = nextElement.getNext();
                    } else {
                        element = new Node<>(e);
                        nextElement.setNext(element);
                        break;
                    }
                }
            } else {
                Node<E> element = new Node<>(e);
                first.setNext(element);
            }
            size++;
        }
    }

    void add(int index, E element) throws OutOfSizeException{
        if(index == 0){
            first = new Node<>(first, element);
        }else if(index <= size){
            Node<E> nextElement = first.getNext();
            for (int i = 1; i < index - 1; i++){
                nextElement = nextElement.getNext();
            }
            if(nextElement.getNext() != null){
                Node<E> nextNextElement = nextElement.getNext();
                nextElement.setNext(nextNextElement);
            }else{
                Node<E> newElement = new Node<>(element);
                nextElement.setNext(newElement);
            }
        }else{
            throw new OutOfSizeException("Unable to add element to the index " + index + ".");
        }
        size++;
    }

    private int getSize() {
        return size;
    }
}
