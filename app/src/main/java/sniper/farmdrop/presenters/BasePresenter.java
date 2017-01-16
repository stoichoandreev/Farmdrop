package sniper.farmdrop.presenters;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<V, R> {

    protected V mView;
    protected R mRepository;
    protected WeakReference<V> reference;
    protected CompositeSubscription mCompositeSubscription;

    protected BasePresenter() {
        mCompositeSubscription = new CompositeSubscription();
    }

    public BasePresenter(V view, R repository) {
        this();
        this.mView = view;
        this.mRepository = repository;
    }

    public void bindView(@NonNull V view) {
        this.reference = new WeakReference<>(view);
    }

    public void unbindView() {
        this.reference = null;
    }

    /**
     * Add subscription to the CompositeSubscription set
     * @param newSubscription - the subscription to be added
     */
    public void addSubscription(Subscription newSubscription){
        if(newSubscription != null){
            mCompositeSubscription.add(newSubscription);
        }
    }

    public void destroyAllSubscriptions(){
        if(mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = new CompositeSubscription();
        }
    }
}