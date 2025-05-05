import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.Stack;
/**
This AVLTree class represents a self-balancing binary search tree that implements Iterable for Iteration.
The AVLTree maintains the height balance property, where the height difference between the left and right subtree of any node is at most one. 
The AVLTree class takes a generic type that is Comparable, which ensures that only comparable values can be added to the tree.
@param <T> the generic type parameter that ensures all elements are of type Comparable
*/
// Joel Canonico T00686800
public class AVLTree<T extends Comparable<T>> implements Iterable<T> 
{
    private Node<T> root; // The root node of the AVLTree
    private int modCount; // keeps track of modifications (adds, removes, remeoveAlls) for fail fasting
    
    /**
    Default constructor for a new AVLTree object with an empty root node and a modification count of zero.
    The root node of the tree is initialized to null, resulting in an empty tree.
    The modCount is set to 0 and is used to keep track of the number of modifications made to the tree.
    */
    public AVLTree()
    {
        root = null;
        modCount = 0;
    }

    /**
    Add a new value to the AVLTree.
    @param value the value being added to the AVLTree
    */
    public void add(T value) 
    {
        root = add(value, root);
        modCount++;
    }

    /**
    Helper method that is private which adds a new value to the AVLTree and
    maintains the height and balance properties by rotating the tree when needed.
    @param value the value being added to the AVLTree
    @param node the node currently in the AVLTree
    @return the new, updated AVLTree node the new value is added
    */
    private Node<T> add(T value, Node<T> node) 
    {
        // if the tree is empty, return a new node that contains the value (this would be the root)
        if (node == null) 
        {
            return new Node<T>(value);
        }
        // if the new value is less than the current value, add as the left child 
        if (value.compareTo(node.getValue()) < 0) 
        {
            node.setLeftChild(add(value, node.getLeftChild()));
        }  
        // if the new value is greater or equal to the current value, add as the right child
        else if (value.compareTo(node.getValue()) >= 0) 
        {
            node.setRightChild(add(value, node.getRightChild()));
        }
    
        // updates the height of the AVL Tree and self-balances/rotates the tree so all values are where they belong
        updateHeight(node);
        return rotateTree(node);
    }
    
    /**
    Remove an value from the AVLTree.
    @param value the value that is removed from the AVLTree
    */
    public void remove(T value) 
    {
        root = remove(value, root);
        modCount++;
    }

    /**
    Helper method that is private which removes an value from the AVLTree and
    maintains the height and balance property by rotating the tree when needed.
    @param value the value that will be removed from the AVLTree
    @param node the node currently inn the AVLTree
    @return the new AVLTree node after the value is removed
    @throws EmptyCollectionException if the AVLTree is empty
    */
    private Node<T> remove(T value, Node<T> node) throws EmptyCollectionException 
    {
        // if the tree is empty, throws the EmptyCollectionException which is handled in TestHarness
        if (isEmpty())
        {
            throw new EmptyCollectionException("AVL Tree");
        }
        // (one child scenario) if the accepted value is less than the node's value, recursively calls the remove function on the left child and returns the value on the left child once the target value is found or null is reached
        if (value.compareTo(node.getValue()) < 0) 
        {
            node.setLeftChild(remove(value, node.getLeftChild()));
        }
        // (one child scenario) if the accepted value is less than the node's value, recursively calls the remove function on the right child and returns the value on the right child once the target value is found or null is reached   
        else if (value.compareTo(node.getValue()) > 0) 
        {
            node.setRightChild(remove(value, node.getRightChild()));
        }
        // (two child scenarios) this block runs if the left child or right child are null, or 
        else 
        {
            // if left child is null, the right child should replace the node
            if (node.getLeftChild() == null) 
            {
                return node.getRightChild();
            } 
            // if the right child is null, the left child should replace the node
            else if (node.getRightChild() == null) 
            {
                return node.getLeftChild();
            } 
            // if neither are null, the inOrderDescendant helper method is called to find the order and the result of the recursive remove call is set as the new right child node 
            else 
            {
                Node<T> inOrderDes = inOrderDescendant(node.getRightChild());
                node.setValue(inOrderDes.getValue());
                node.setRightChild(remove(inOrderDes.getValue(), node.getRightChild()));
            }
        }
    
        // updates the height of the AVL Tree and self-balances/rotates the tree so all values are where they belong
        updateHeight(node);
        return rotateTree(node);
    }
    
    /**
    Removes all occurrences of a specified value from the AVL tree.
    @param value the instances of a value to be removed from the AVL tree
    @throws EmptyCollectionException if the AVL tree is empty
    */
    public void removeAll(T value) throws EmptyCollectionException 
    {
        root = removeAll(value, root);
        modCount++;
    }

    /**
    Private helper method that recursively removes all occurrences of a specified value from the AVL tree rooted at the given node.
    @param value the value instances to be removed from the AVL tree
    @param node the root of the AVL tree or its subtree to be searched for 
    @return the new root of the AVL tree or its subtree after all occurances of the value are removed
    @throws EmptyCollectionException if the AVL tree is empty
    */
    private Node<T> removeAll(T value, Node<T> node) throws EmptyCollectionException 
    {
        // if the tree is empty, throws the EmptyCollectionException which is handled in TestHarness
        if (isEmpty()) 
        {
            throw new EmptyCollectionException("AVL Tree");
        }

        // if the node is null, returns null so that the code below does not attempt to set left and right childs based on a null node
        if (node == null) 
        {
            return null;
        }

        // this will check the entire left sub tree and remove all nodes containing the inputted T value
        node.setLeftChild(removeAll(value, node.getLeftChild()));
    
        // this will check the entire right subtree and remove all nodes containing the inputted T value
        node.setRightChild(removeAll(value, node.getRightChild()));
    
        // if the inputted value and value of the current node are the same, remove the node
        if (value.compareTo(node.getValue()) == 0) 
        {
            // one child or no child/leaf node scenarios
            if (node.getLeftChild() == null) 
            {
                return node.getRightChild();
            } 
            else if (node.getRightChild() == null) 
            {
                return node.getLeftChild();
            } 
            // two children scenario
            else 
            {
                // if the node has two children, find the descendants (in order) using the helper method
                Node<T> inOrderDes = inOrderDescendant(node.getRightChild());
                node.setValue(inOrderDes.getValue());
                node.setRightChild(removeAll(inOrderDes.getValue(), node.getRightChild()));
            }
        }
        // updates the height of the AVL Tree and self-balances/rotates the tree so all values are where they belong
        updateHeight(node);
        return rotateTree(node);
    }
    
    /**
    Finds and then returns all of the in-order descendants of the provided node.
    @param node the node in which its descendants in-order need to be found.
    @return the in-order descendants of the provided node
    */
    private Node<T> inOrderDescendant(Node<T> node) 
    {
        // while the left child isn't null, set the node to the next left child
        while (node.getLeftChild() != null) 
        {
            node = node.getLeftChild();
        }

        // returns the in order descendant of the given node
        return node;
    }

    /**
    Returns true if the AVL tree contains the inputted generic value.
    @param value the generic value that will be searched for in the AVL Tree. 
    @return true if the AVL tree contains the inputted generic value, false otherwise
    */
    public boolean contains(T value) 
    {
        return contains(value, root); // returns the helper method boolean result
    }

    /**
    Private helper method which recursively searches for the inputted generic value in the AVL tree at the provided node.
    @param value the generic value to be searched for in the AVL tree
    @param node the root node of the AVL tree or its subtree that need to be searched for the value
    @return true if the AVL tree contains the inputted generic value, false otherwise
    */
    private boolean contains(T value, Node<T> node) 
    {
        // if the tree is empty there is no way it can contain the value
        if (node == null) 
        {
            return false;
        }
        // if the inputted value is less than the current node value, recursively call the contains method on the left child until the element is found or is null
        if (value.compareTo(node.getValue()) < 0) 
        {
            return contains(value, node.getLeftChild());
        }
        // if the inputted value is less than the current node value, recursively call the contains method on the right child until the element is found or is null 
        else if (value.compareTo(node.getValue()) > 0) 
        {
            return contains(value, node.getRightChild());
        }
        // this else block indicates that the value is the same as the current node's value, thus returns true
        else 
        {
            return true;
        }
    }

    /**
    Checks whether the AVL tree is empty or not based on the root being null, or not.
    @return true if the tree root is null, false otherwise
    */
    public boolean isEmpty()
    {
        return root == null;
    }

    /**
    Rotates the tree by balancing a provided node when needed.
    @param node the node to be balanced
    @return the node after it has been balanced
    */
    private Node<T> rotateTree(Node<T> node) 
    {
        int balance = balance(node); // int holding the result of the balance helper method
    
        // this occurs if the left sub tree is too low and uses the rotate tree left method which accepts the left child node
        if (balance > 1) 
        {
            if (balance(node.getLeftChild()) < 0) 
            {
                node.setLeftChild(rotateTreeLeft(node.getLeftChild())); // rotates the tree left with the left child node - tree is kind of "half rotated" at this point 
            }
            return rotateTreeRight(node); // rotates the tree right with the provided node - tree should be fully balanced (balance = 0) at this point
        }
    
        // this occurs if the right sub tree is too low and calls the rotate tree right method which accepts the right child node to set the balance to 0 (balanced)
        if (balance < -1) 
        {
            if (balance(node.getRightChild()) > 0) 
            {
                node.setRightChild(rotateTreeRight(node.getRightChild())); // rotates the right left with the right child node - tree is kind of "half rotated" at this point
            }
            return rotateTreeLeft(node); // rotates the tree left with the provided node - tree should be fully balanced (balance = 0) at this point
        }
        return node; // returns the node because this method is used in add, remove, and removeAll methods which require a node
    }

    /**
    Rotate's the AVL Tree right by rotating the given node to the right.
    @param node the node that is sent to rotateTree
    @return the new root of the sub subtree after the tree has been rotated right
    */
    private Node<T> rotateTreeRight(Node<T> node)
    {
        // sets the left child node as the new root of the sub tree, the center node becomes the new left child, and the original node becomes the right child
        Node<T> leftChild = node.getLeftChild();
        Node<T> centerNode = leftChild.getRightChild();
        leftChild.setRightChild(node);
        node.setLeftChild(centerNode);

        // updates the heights and returns the left child node (new root of the sub tree)
        updateHeight(node);
        updateHeight(leftChild);
        return leftChild;
    }

    /**
    Rotate's the AVL Tree left by rotating the given node to the left.
    @param node the node that is sent to rotateTree
    @return the new root of the sub subtree after the tree has been rotated left
    */
    private Node<T> rotateTreeLeft(Node<T> node)
    {
        // makes the right child node the new root of the sub tree, the original node becomes the left child, and the center node becomes the right child of the original node
        Node<T> rightChild = node.getRightChild();
        Node<T> centerNode = rightChild.getLeftChild();
        rightChild.setLeftChild(node);
        node.setRightChild(centerNode);

        // updates the heights and returns the right child node (new root of the sub tree)
        updateHeight(node);
        updateHeight(rightChild);
        return rightChild;
    }

    /**
    Updates the height of the provided node based on the maximum height between its children
    @param node the node height to update
    */
    private void updateHeight(Node<T> node)
    {
        int maxHeight = Math.max(height(node.getLeftChild()), height(node.getRightChild())); // determines the maximum value (height) by comparing the heights of the left child node, right child node
        node.setHeight(maxHeight + 1); // sets the height to the max height calculation, and adds 1 because the height of a tree is 1 more than the path to its furthest child
    }

    /**
    Calculates the balance value of the provided node.
    @param node the node to calculate the balance value for
    @return the balance value of the node
    */
    private int balance(Node<T> node) 
    {
        // if node is null, balance is 0
        if (node == null) 
        {
            return 0;
        } 
        // else determine the left and right child heights and return the difference 
        else 
        {
            int leftHeight = height(node.getLeftChild());
            int rightHeight = height(node.getRightChild());
            return leftHeight - rightHeight;
        }
    }

    /**
    Returns the height of the provided node.
    @param node the node to calculate the height for
    @return the height value of the node
    */
    private int height(Node<T> node) 
    {
        // if the node is empty, height must be 0
        if (node == null) 
        {
            return 0;
        }
        // else return the getHeight method from the node class 
        else 
        {
            return node.getHeight();
        }
    }
 
    /**
    Returns a string representation of this AVLTree's node values. 
    The toString provides an in-order traversal of the tree by using a recursive method to search the entire left side of the tree, and then the right.
    The values in the resulting string are separated by a space character.
    @return a string representation of the contents of an AVLTree
    */
    public String toString() 
    {
        StringBuilder buildString = new StringBuilder(); // declaring StringBuilder reference variable
        traverseTreeInOrder(root, buildString); // calls the in order tree traversal helper method which builds the toString of in order tree elements
        return buildString.toString(); // calls the toString of the StringBuilder reference variable, which returns an in order toString of the AVL Tree's contents
    }
    
    /**
    Recursive helper method that performs a traversal in order of the AVLTree.
    The entire left side of the tree is recursively searched first, with each node's values being appended to a string, followed by the right side.
    @param node the current node in the AVLTree
    @param buildString the StringBuilder to append each in order element with a space in between
    */
    private void traverseTreeInOrder(Node<T> node, StringBuilder buildString) 
    {
        // while the node isn't null, recursively calls itself to get the left child, appends its value, adds a space, and then recursively calls itself again for the right child
        if (node != null) 
        {
            traverseTreeInOrder(node.getLeftChild(), buildString); // recursive call on the left child until it is null
            buildString.append(node.getValue()); // adds node value to the string
            buildString.append(" "); // adds a space in between each node value
            traverseTreeInOrder(node.getRightChild(), buildString); // recursive call on the right child until it is null
        }
    }
    
    /**
    * Returns an iterator over the values in the AVL tree. 
    * The values are traversed in-order.
    * @return an iterator over the values in the AVL tree
    */
    public Iterator<T> iterator() 
    {
        Stack<Node> stack = new Stack<>(); // creates a stack which will push and pop Node values during and after iteration
        Node current = root; // current is set to the root by default
        final int expectedModCount = modCount; // sets expected mod count to the value stored in mod count determined through adds and removes in the AVL Tree

        // while current is not null, pushes current onto the stack and current's value is set to its left child until null is reached
        while (current != null) 
        {
            stack.push(current);
            current = current.getLeftChild();
        }   

        // returns the iterator
        return new Iterator<T>() 
        {
            // *method overriding and fail fast implementation begins here* //

            private Node recentNode; // Node variable to keep track of the node that was most recently iterated on
            private int iterModCount = expectedModCount; // iterator mod count for fail fasting

            /**
            * Will return true if there are more elements in the AVL Tree to iterate, false if the stack is empty
            * @return true if there are more values in the AVL tree to iterate over, false otherwise
            * @throws ConcurrentModificationException if the AVL tree is modified during an iteration it will fail fast
            */
            // overriding hasNext method to throw an exception for fail fasting if the iterator mod count and expected mod count are not the same
            public boolean hasNext() 
            {
                if (iterModCount != expectedModCount) 
                {
                    throw new ConcurrentModificationException(); // program fail fasts if the mod counts are different
                }
                return !stack.isEmpty(); // boolean return value based on if the stack used to iterate through the elements is empty or not
            }

            /**
            * Returns the next value found in the AVL tree.
            * @return the next value found in the AVL tree
            * @throws NoSuchElementException if there are no more values to iterate over (iteration stops)
            * @throws ConcurrentModificationException if the AVL tree has been modified while the iterator is in use (fail fasting)
            */
            // overrides next method to determine what next value of this AVL Tree is
            public T next() 
            {
                if (!hasNext()) 
                {
                    throw new NoSuchElementException(); // throws an exception if there are no more elements to iterate (stops iteration)
                }
                recentNode = stack.pop(); // pops an element from the stack and stores it in last returned variaible
                Node current = recentNode.getRightChild(); // stores the current Node as and right child of the last returned node

                // as long as the current Node is not null, this pushes the current node onto the stack and the new current node is set to current's left child
                while (current != null) 
                {   
                    stack.push(current);
                    current = current.getLeftChild();
                }
            
                iterModCount = modCount; // sets the iterator mod count to the mod count declared at the start of the class. if these values are ever not equal the program will fail fast
                return (T)recentNode.getValue(); // casts the last returned value to type T to ensure type checking at compile time
            }
        };
    }
}