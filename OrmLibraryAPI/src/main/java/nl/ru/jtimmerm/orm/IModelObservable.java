
package nl.ru.jtimmerm.orm;

public interface IModelObservable {
  void addModelListener(IModelListener mcl) ;

  void removeModelListeners(IModelListener mcl) ;

  void removeAllListeners(IModelListener mcl) ;

}
