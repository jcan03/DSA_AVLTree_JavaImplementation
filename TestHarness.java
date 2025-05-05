import java.util.Iterator;
import java.util.ConcurrentModificationException;
/**
The TestHarness class is a driver program that tests the functionality of the AVLTree class. 
It adds, removes, and removes all occurrences of elements from the AVL tree, and prints the 
toString of the AVL tree before and after these operations. It will also show examples of 
a successful iteration through the AVLTree, fail fasting occuring during an iteration, and
an EmptyCollectionException being handled after attempting to remove from an empty tree.
*/
// Joel Canonico T00686800
public class TestHarness
{
    public static void main(String[] args)
    {
        try
        {
            // defines test tree and adds four values to it, one being a duplicate
            AVLTree<Integer> tree = new AVLTree<>();    
            tree.add(1);
            tree.add(2);
            tree.add(3);
            tree.add(3); // shows successful adds

            // tree elements prior to a remove - shows working toString
            System.out.println(tree.toString());
            tree.remove(1); // remove 1

            // tree elements after removing 1
            System.out.println(tree.toString());

            // testing removeAll method on 3
            tree.removeAll(3);

            // tree elements to confirm that both 3's were removed
            System.out.println(tree.toString());

            // adding a couple elements to test the contains method
            tree.add(1);
            tree.add(2);

            System.out.println(tree.contains(1)); // should return true
            System.out.println(tree.contains(12)); // should return false
            System.out.println(tree.contains(2)); // should return true
            System.out.println(tree.contains(-2)); // should return false

            // creates an iterator for the tree
            Iterator<Integer> iterator = tree.iterator();

            // testing a successful iteration through the AVLTree
            while (iterator.hasNext())
            {
                System.out.println(iterator.next());
            }

            // testing EmptyCollectionException
            tree.remove(1);
            tree.removeAll(2);
            tree.remove(3); // this call will throw the EmptyCollectionException
        }
        // if an EmptyCollectionException is thrown in the AVLTree class, the appropriate message is outputted
        catch (EmptyCollectionException e)
        {
            System.out.println(e.getMessage());
        }

        // this try catch block is to test fail fasting only
        try 
        {
            // creates another tree, adds some int values
            AVLTree<Integer> tree1 = new AVLTree<>();
            tree1.add(1);
            tree1.add(2);
            tree1.add(3);

            // creates an iterator for the tree object
            Iterator<Integer> iterator1 = tree1.iterator();

            // fail fast test
            while (iterator1.hasNext()) 
            {
                Integer value = iterator1.next();

                if (value == 2) 
                {                        
                    tree1.add(4); // this will throw the ConcurrentModificationException for fail fasting
                }
            }
        }
        // prints a message indicating fail fasting has occured if it is thrown from the AVLTree class
        catch (ConcurrentModificationException e)
        {
            System.out.println("ConcurrentModificationException has occured --> (Fail Fast Test)");
        }
    }
}