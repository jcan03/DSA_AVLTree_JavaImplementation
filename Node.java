/**
This node class represents a node in an AVL Tree, storing an value of type T that must be Comparable.
The class provides methods to retrieve and modify (getters and setters) for the value, height, left child, and right child of the node.
@param <T> is the generic type of value stored in the node, which must implement Comparable.
*/
// Joel Canonico T00686800
public class Node<T extends Comparable<T>> 
{
    // declaring variables for a Node
    private T value;
    private int height = 1; // initial height is set to 1
    protected Node<T> leftChild, rightChild;
    
    /**
    Constructs a new node with the specified value.
    @param value the value to store in the node
    */
    public Node(T value) 
    {
        this.value = value;
    }

    /**
    Returns the left child of this node.
    @return the left child of this node
    */
    public Node<T> getLeftChild() 
    {
        return leftChild;
    }

    /**
    Returns the right child of this node.
    @return the right child of this node
    */
    public Node<T> getRightChild() 
    {
        return rightChild;
    }

    /**
    Returns the value stored in this node.
    @return the value stored in this node
    */
    public T getValue() 
    {
        return value;
    }

    /**
    Returns the height of this node.
    @return the height of this node
    */
    public int getHeight() 
    {
        return height;
    }

    /**
    Sets the value stored in this node.
    @param newValue the new value to store in this node
    */
    public void setValue(T newValue) 
    {
        this.value = newValue;
    }

    /**
    Sets the right child of this node.
    @param newRightChild the new right child of this node
    */
    public void setRightChild(Node<T> newRightChild) 
    {
        this.rightChild = newRightChild;
    }

    /**
    Sets the left child of this node.
    @param newLeftChild the new left child of this node
    */
    public void setLeftChild(Node<T> newLeftChild) 
    {
        this.leftChild = newLeftChild;
    }

    /**
    Sets the height of this node.
    @param newHeight the new height of this node
    */
    public void setHeight(int newHeight) 
    {
        this.height = newHeight;
    }
}