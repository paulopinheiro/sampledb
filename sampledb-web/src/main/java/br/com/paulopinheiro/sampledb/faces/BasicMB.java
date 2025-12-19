package br.com.paulopinheiro.sampledb.faces;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import java.util.List;
import java.util.Optional;

public abstract class BasicMB<T> {
    private T entity;

    public abstract boolean isNewEntity();
    protected abstract void saveEntity(T entity);
    protected abstract void deleteEntity(T entity);
    public abstract List<T> getList();
    protected abstract T newEntityInstance();

    public void clear(ActionEvent evt) {
        setEntity(null);
    }

    public void save(ActionEvent evt) {
        try {
            saveEntity(getEntity());
            successMessage(getEntity().toString() + " saved successfully.");
            clear(evt);
        } catch (Exception ex) {
            errorMessage(ex.getMessage());
        }
    }

    public void delete(ActionEvent evt) {
        try {
            deleteEntity(this.getEntity());
            successMessage(this.getEntity().toString() + " removed successfully.");
            clear(evt);
        } catch (Exception ex) {
            errorMessage(ex.getMessage());
        }
    }

    protected T getEntity() {
        if (Optional.ofNullable(entity).isEmpty())
            entity = newEntityInstance();
        return entity;
    }
    protected void setEntity(T entity) {this.entity = entity;}

    protected void errorMessage(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,mensagem,null));
    }

    protected void successMessage(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,mensagem,null));
    }

    protected void warningMessage(String mensagem) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,mensagem,null));
    }
}
