
class List {

	class Node {
		public int elem;
		public Node next;

		public Node(int n) {
			elem = n;
			next = null;
		}
				
	}

	Node first = null;

	public List cons(int n) {
		Node node = new Node(n);
		node.next = first;
		first = node;
		return this;
	}

	public List tail() {
		if (first != null) 
			first = first.next;
		return this;
	}	

	public int getFirst() {
		if (first != null) 
			return first.elem;
		return -1;
	}
}

public class Stack {

	List list;

	public Stack() {
		list = new List();
	}

	public Stack push(int n) {
		list.cons(n);
		return this;
	}

	public Stack pop() {
		list.tail();
		return this;
	}

	public int top() {
		return list.getFirst();
	}


	static public void main(String[] args) {
		Stack stack = new Stack();
		stack.push(1).push(2);
		System.out.println(stack.top());
		stack.pop();
		System.out.println(stack.top());
	}
}


