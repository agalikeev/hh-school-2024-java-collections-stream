package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй

  // Лучше использовать проверку на пустоту методом isEmpty, так меньшей операций и читабельнее
  public List<String> getNames(List<Person> persons) {
    if (persons.isEmpty()) {
      return Collections.emptyList();
    }
    // Первый элемент можно пропустить в стриме, а не удалять заранее
    return persons.stream().skip(1).map(Person::firstName).collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // set в любом случае оставит только уникальные значения, стрим тут не нужен
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    // Можно в стриме объединять ФИО, проверяя при этом значения на null, не разделяя всё на 3 проверки вручную
    return Stream.of(person.firstName(), person.secondName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    // Можно проверять на уникальность и создавать словарь в стриме
    return persons.stream()
        .distinct()
        .collect(Collectors.toMap(Person::id, this::convertPersonToString));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    // Можно заменить на приведение к множеству и нахождения пересечений между двумя множествами
    Set<Person> person1Set = new HashSet<>(persons1);
    Set<Person> person2Set = new HashSet<>(persons2);
    person1Set.retainAll(person2Set);
    return !person1Set.isEmpty();
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    // Можно посчитать количество элементов после применения фильтра прямо в стриме
    count = numbers.filter(num -> num % 2 == 0).count();
    return count;
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    // Проверил через отладку. Элементы в set оказываются упорядочены изначально, дело не в toString.
    // При создании HashSet наши числа записываются в бакеты, а номера бакетов хранятся по возрастанию и в этом порядке и возвращаются сетом.
    // Смущает то, что нет пустых бакетов для 10000 элементов - заполнены первые 10000 бакетов, хотя число бакетов - это обычно степень двойки
    // Тут либо число бакетов реально 10000 (что мало похоже на правду), либо мы убираем незанятые бакеты и перенумеруем все бакеты, чтобы не было дыр между ними
    // Но тогда непонятно как добавлять потом новые элементы. Если двигать после хеширования постоянно, то как будто слишком дорого становится
    // Не нашёл точной информации по этому поводу, но думаю, что примерно так всё работает
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
