/**
EmptyCollectionException is a custom exception class that extends RuntimeException. 
It is thrown when attempting to perform an operation (generally a remove) on an empty collection.
Provides a new EmptyCollectionException with a default error message and a method for a parameterized constructor.
*/
// Joel Canonico T00686800
public class EmptyCollectionException extends RuntimeException
{
    // default constructor
    public EmptyCollectionException()
    {
    super("The collection is empty.");
    }

    /**
    Constructs a new EmptyCollectionException for a specific collection being empty
    @param message the type of collection that is empty
    */
    public EmptyCollectionException(String message)
    {
        super("The " + message + " is empty.");
    }
}
