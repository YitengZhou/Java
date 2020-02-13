package com.company;

class Dog{
    private Owner owner;
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        if (this.owner!=null)
            this.owner.setDog(null);
        this.owner = owner;
        owner.setDog(this);
    }
}
public class Main {
    public static void main(String[] args) {
	Owner owner1= new Owner();
	Owner owner2= new Owner();
	Dog dog1=new Dog();
	dog1.setOwner(owner1);
	dog1.setOwner(owner2);
    }
}
