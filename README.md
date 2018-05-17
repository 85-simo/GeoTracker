# **Architectural Overview**

The app has been designed and developed in a way that aims at merging the MVVM architectural pattern with Clean Architecture's principles. The whole software makes extensive use of the Android newest architectural components (such as Room, ViewModels. LiveData), RxJava2, Dagger2 and Butterknife and may be represented as separated into 3 main layers:

 - Data Layer
 - Domain Layer
 - Presentation Layer
Separation between layers is strict and, as such, they're only allowed to communicate through interfaces and RxJava objects.

## The Data Layer

The data layer is responsible for all I/O operations - that is, persistence and retrieval of data from the DB storage engine.
In order to centralize data-related operations and to abstract DB engine interaction from the operations' logic, all interactions with this layer happen through a class implementing a (simplified) Repository pattern. The repository itself only exposes a set of operations through an interface.

## The Domain Layer

The role of the domain layer is gluing together both the data and presentation layers: it defines generic interfaces for defining all types of interactions allowed between the presentation and data layer and is consituted by a set of concrete interactor classes, each of which implements a single interface (thus exposing a single operation to the presentation layer).
Each interactor's inner logic may require combining multiple simpler interactors in order to be effectively implemented: for instance, an interactor exposing a retrieve operation may internally need to combine operations provided by other interactors in order to complete. This has been done through composition of interactors instead of inheritance: in fact, interactors represent the app's business logic and need to be kept as safe as possible from future changes that could impact them significantly otherwise.

## The Presentation Layer

In our case, the app's presentation layer is constituted by ViewModels. They encapsulate presentation logic and are generally specific of a certain view (this might not hold true for cases in which, for instance, a certain activity needs to communicate with contained fragments). They interact with the domain layer through direct invocation of operations exposed by basic interactor interfaces, receiving RxJava obserble objects in return. On the other side ViewModel->View communication is handled through LiveData (either Mutable or Single): since in a MVVM pattern ViewModels are supposed to asynchronously notify Views of events, and since ViewModels share no knowledge of a certain view state (with respect to Android Views lifecycle), communicating through LiveData observable streams makes it much safer to properly notify a listening view of a certain event. LiveData object in fact automatically register/unregister observers depending on the actual lifecycle state. Additionally (at least for MutableLiveData streams) the framework takes care of redelivering events on view state changes.
