package tasks;

import common.Person;
import common.PersonService;

import java.util.*;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы


Асимптотика добавления и получения элемента у Map - O(1)
Добавляем N элементов в Map - O(N)
Проходимся по списку personIds из N элементов и получаем для каждого элемента значение из Map idPersonsMap по ключу - O(N)

Итоговая асимптотика O(N)

Изначально у меня была O(N^2)
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);

    Map<Integer, Person> idPersonsMap = persons.stream()
        .collect(Collectors.toMap(Person::id, person -> person));

    return personIds.stream()
        .map(idPersonsMap::get)
        .toList();
  }
}
