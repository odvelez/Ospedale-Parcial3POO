package core.models.storage;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import java.util.ArrayList;

public class ModelChangeNotifier {

    private static ModelChangeNotifier instance;

    private final ArrayList<ModelChangeListener> listeners;

    private ModelChangeNotifier() {
        this.listeners = new ArrayList<>();
    }

    public static ModelChangeNotifier getInstance() {
        if (instance == null) {
            instance = new ModelChangeNotifier();
        }
        return instance;
    }

    public void addListener(ModelChangeListener listener) {
        if (listener == null) {
            return;
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ModelChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyChange(ModelChangeType type) {
        ArrayList<ModelChangeListener> snapshot = new ArrayList<>(listeners);
        for (ModelChangeListener listener : snapshot) {
            listener.onModelChanged(type);
        }
    }
}
