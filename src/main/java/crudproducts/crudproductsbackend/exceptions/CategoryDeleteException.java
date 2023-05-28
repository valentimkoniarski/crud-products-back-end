package crudproducts.crudproductsbackend.exceptions;

public class CategoryDeleteException extends Exception {


    public CategoryDeleteException() {
        super("Cannot delete category because it is linked to a product.");
    }


}

