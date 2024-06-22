
package com.leiloes.application.model.dao;

import com.leiloes.application.model.entities.JPAUtil;
import com.leiloes.application.model.entities.Produtos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class ProdutosDAO {
    
    public void cadastrar(Produtos p) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public void excluir(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Produtos p = em.find(Produtos.class, id);
            if (p != null) {
                em.getTransaction().begin();
                em.remove(p); 
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public List<Produtos> listar(Integer filtroId, String filtroNome, Double filtroValor,
            String filtroStatus) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Produtos> produtos = null;
        try {
            String textoQuery = "SELECT p FROM Produtos p "
                    + "WHERE (:idFiltro = -1 OR p.id >= :idFiltro) "
                    + "AND (:nomeFiltro IS NULL OR p.nome LIKE :nomeFiltro) "
                    + "AND (:valorFiltro IS NULL OR p.valor >= :valorFiltro) "
                    + "AND (:statusFiltro IS NULL OR p.nome LIKE :statusFiltro) ";

            Query consulta = em.createQuery(textoQuery);

            consulta.setParameter("idFiltro", filtroId);
            consulta.setParameter("nomeFiltro", filtroNome.isEmpty() ? null : "%" + filtroNome + "%");
            consulta.setParameter("valorFiltro", filtroValor);
            consulta.setParameter("statusFiltro", filtroStatus.isEmpty() ? null : "%" + filtroStatus + "%");
            

            produtos = consulta.getResultList();
        } catch (Exception ex) {
            System.err.println("Erro" + ex.getMessage());
        } finally {
            JPAUtil.closeEntityManager();
        }
        return produtos;
    }

    public void atualizar(Produtos p) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }
    
   public List<Produtos> obterId(Integer filtroId) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        Query query = em.createQuery("SELECT p FROM Produtos p WHERE p.id = :filtroId", Produtos.class);
        query.setParameter("filtroId", filtroId);
        return query.getResultList();
    } finally {
        JPAUtil.closeEntityManager();
    }
}
   public void venderProduto(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Produtos p = em.find(Produtos.class, id);
            if (p != null) {
                em.getTransaction().begin();
                p.setStatus("Vendido");
                em.merge(p);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            JPAUtil.closeEntityManager();
        }
    }
   
}
