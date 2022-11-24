package lectures.part5TypeSystem

object Variance extends App{

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance? - type substitution of generics
  class Cage[T]

  // covariance
  class CCage[+T] // covariant cage
  val ccage: CCage[Animal] = new CCage[Cat]

  //  invariant
  class ICage[T]
  //val icage: ICage[Animal] = new ICage[Cat]
  // val x: Int = "hello"

  // opposite - contravariance
  class XCage[-T]
  val xcage: XCage[Cat] = new XCage[Animal]

  class IntravariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T)

  // contravariant
  //class ContravariantCage[-T](val animal: T)

  //class CovariantVariableCage[+T](var animal: T)

  /*trait AnotherCovariantCage[+T] {
    def addAnimal(animal: T) // contravairant position
  }*/

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]

  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type, super type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add( new Kitty )
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog) // now gets widens to 'Animal'

  // return types
  class PetShop[-T] {
    //def get(isItAPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  class TerraNova extends Dog

  val bigFurry = shop.get(true, new TerraNova)

  // method arguments are in contravariant position
  // return types are in covariant position

  class Vehicle
  class Bikes extends Vehicle
  class Cars extends Vehicle
  class IList[T]

  // invariant parking
  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  // covariant
  class CParking[+T](vehciles: List[T]){
    def park[S >: T](vehcile: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???
  }

  // contravariant
  class XParking[-T](vehicles: List[T]){
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: Function1[R, XParking[S]]): IParking[S] = ???

  }

  // covariance - collection of things
  // group of actions - contravariance





}
