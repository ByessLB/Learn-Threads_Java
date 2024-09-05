# Les Threads

## Introduction

### Qu'est ce qu'un Thread ?

Un thread (ou fil d'exécution) est la plus petite unité d'exécution dans un programme. En Java, un thread est une séquence d'instruction exécutées de manière indépendante et concurrente. Les threads permettent d'exécuter plusieurs tâches en parallèle, ce qui peut améliorer les performances et la réactivité des applications.

### Pourquoi utiliser des Threads ?

- **Performance** : Permet d'exécurter plusieurs tâches en parallèle, améliorant ainsi les performances.
- **Réactivité** : Permet de maintenir l'interface utilisateur réactive en exécutant des tâches longues dans des threads séparés.
- **Utilisation des ressources** : Permet d'utiliser efficacement les ressources du système, comme les processeurs multicoeurs.

## Création et Gestion des Threads

### Création d'un Thread

Il existe 2 principales manières de créer un thread en Java :

1. **En héritant de la classe `thread`**
2. **En Implémentant l'interface `Runnable`**

#### Héritage de la Classe `Thread`
```java
public class MonThread extedns Thread {
    @Override
    public void run() {
        System.out.println("Thread en cours d'exécution : " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        MonThread monThread = new MonThread();
        monThread.start();
    }
}
```

#### Implémentation de l'Interface `Runnable`
```java
public class MonRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread en cours d'exécution : " + Thread.currentThread().getName());
    }
}

public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(new MonRunnable());
        thread.start();
    }
}
```

### Démarrage d'un Thread

Pour démarrer un thread, utilisez la méthode `start()`. Cette méthode apelle la méthode `run()` dans un nouveau thread.
```java
public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(new MonRunnable());
        thread.start();
    }
}
```

### Arrêt d'un Thread

Il n'est pas recommandé d'utiliser la méthode `stop()` pour arrêter un thread, car elle est dépréciée et peut causer des problèmes de sécurité. A la place, utiliser un indicateur pour signaler au thread qu'il doit s'arrêter.
```java
public class MonRunnable implements Runnable {
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            System.out.println("Thread en cours d'exécution : " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrompu");
            }
        }
    }

    public void stop() {
        running = false;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MonRunnable runnable = new MonRunnable();
        Thread thread = new Thread(runnable);
        thread.start();

        thread.sleep(5000); // Laisser le thread s'exécuter pendant 5s
        runnable.stop();
    }
}
```

## Synchronisation des Threads

### Problème de la Concurrence

Lorsque plusieurs threads accèdent à des resources partagées, des problèmes de concurrence peuvent survenir, comme les conditions de course (race conditions) et les interblocages (deadlocks).

### Utilisation de `synchronized`

Pour éviter les problèmes de concurrence, utilisez le mot-clé `synchronized` pour synchroniser l'accès aux ressources partagées.
```java
public class Compteur {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Compteur compteur = new Compteur();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                compteur.increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Count : " + compteur.getCount());
    }
}
```

### Utilisation de `ReentrantLock`

Une alternative à `synchronized` est l'utilisation de `ReentrantLock` pour une synchronisation plus fine.
```java
import java.util.concurrent.locks.ReentranLock;

public class Compteur {
    private int count = 0;
    private final ReentranLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Compteur compteur = new Compteur();

        Runnavble task = () -> {
            for (int i = 0; i < 1000; i++) {
                compteur.increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Count : " + compteur.getCount());
    }
}
```

## Communication entre Threads

### Utilisation de `wait()` et `notify()`

Les méthodes `wait()` et `notify()` permettent de synchroniser les threads en attendant des conditions spécifiques.
```java
public class Compteur {
    private int count = 0;

    public synchronized void increment() {
        count++;
        notify{};
    }

    public synchronized void waitForCount(int target) throws InterruptedException {
        while (count < target) {
            wait();
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Compteur compteur = new Compteur();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                compteur.increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        compteur.waitForCount(2000);

        System.out.println("Count : " + compteur.getCount());
    }
}
```

## Concllusion

Les threads en Java sont un moyen puissant de créer des applications concurrentes et réactives. En utilisant les threads, vous pouvez améliorer les performances et la réactivité de vos applications. Cependant, il est important de gérer correctement la synchronisation et la communication entre les threads pour éviter les problèmes de concurrence.