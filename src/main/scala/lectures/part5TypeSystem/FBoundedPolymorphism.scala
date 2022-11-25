package lectures.part5TypeSystem

object FBoundedPolymorphism extends App{

  // Solution 1: Naive
  /*trait Animal {
    def breed: List[Animal]
  }

  class Cat extends Animal {
    override def breed: List[Animal] = ???
  }

  class Dog extends Animal {
    override def breed: List[Dog] = ???
  }*/

  // solution 2 - FBP
  /*trait Animal[A <: Animal[A]] { // recursive type, f-bounded poly-morhpism
    def breed: List[Animal[A]]
  }

  class Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]] = ???
  }

  class Dog extends Animal[Dog] {
    override def breed: List[Animal[Dog]] = ???
  }

  trait Entity[E <: Entity[E]] // ORM
  class Person extends Comparable[Person] {
    override def compareTo(o: Person): Int = ???
  }

  class Crocodile extends Animal[Dog] {
    override def breed: List[Animal[Dog]] = ???
  }*/

  // Solution No 2
  /*trait Animal[A <: Animal[A]] { self: A => // recursive type, f-bounded poly-morhpism
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Animal[Cat]] = ???
    }

    class Dog extends Animal[Dog] {
      override def breed: List[Animal[Dog]] = ???
    }

    trait Entity[E <: Entity[E]] // ORM
    class Person extends Comparable[Person] {
      override def compareTo(o: Person): Int = ???
    }

    //class Crocodile extends Animal[Dog] {
    //  override def breed: List[Animal[Dog]] = ???
    //}
    trait Fish extends Animal[Fish]
    class shark  extends Fish {
      override def breed: List[Animal[Fish]] = List(new Cod)
    }

    class Cod extends Fish {
      override def breed: List[Animal[Fish]] = ???
    }*/

    // Solution No 4: Type classes
    trait Animal
    trait CanBreed[A] {
      def breed(a: A): List[A]
    }

    class Dog extends Animal
    object Dog {
      implicit object DogsCanBreed extends CanBreed[Dog] {
        def breed(a: Dog): List[Dog] = List()
      }
    }

    implicit class CanBreedOps[A](animal: A) {
      def breed(implicit canBreed: CanBreed[A]): List[A] =
        canBreed.breed(animal)
    }

    val dog = new Dog
    dog.breed // List[Dogs]

    class Cat extends Animal
    object Cat {
      implicit object CatsCanBreed extends CanBreed[Dog] {
        def breed(a: Dog): List[Dog] = List()
      }
    }

    val cat = new Cat
    //cat.breed


}
