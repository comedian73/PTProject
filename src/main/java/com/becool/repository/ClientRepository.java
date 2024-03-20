package com.becool.repository;

import com.becool.model.NewClient;
import com.becool.db.tables.pojos.Client;
import com.becool.db.tables.records.ClientRecord;
import com.becool.model.UpdateClient;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.fominsky.becool.db.tables.Client.CLIENT;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.val;

@Repository
@RequiredArgsConstructor
public class ClientRepository {

    private final DSLContext dslContext;

    public Client createNewClient(NewClient client) {
        return this.dslContext.insertInto(CLIENT)
                .columns(
                        CLIENT.ID,
                        CLIENT.NAME,
                        CLIENT.EMAIL
                ).values(
                        client.getId(),
                        client.getName(),
                        client.getEmail()
                ).returning()
                .fetchOne()
                .map(r -> r.into(Client.class));
    }

    public Optional<Client> getClientById(String id) {return this.get(id, CLIENT.ID);}

    public Optional<Client> getClientByEmail(String email) {return this.get(email, CLIENT.EMAIL);}

    public List<Client> getClientList() {
        try (Stream<ClientRecord> stream = this.dslContext.selectFrom(CLIENT)
                .where(CLIENT.DELETED.eq(false))
                .fetch()
                .stream()) {
            return stream
                    .map(r -> r.into(Client.class))
                    .toList();
        }
    }

    public Optional<Client> updatePhone(UUID id, String phone) {
        return this.dslContext.update(CLIENT)
                .set(CLIENT.PHONE, phone)
                .where(CLIENT.ID.eq(id))
                .returning()
                .fetchOptional()
                .map(r -> r.into(Client.class));
    }

    public Optional<Client> updateClient(UUID id, UpdateClient updateClient) {
        return this.dslContext.update(CLIENT)
                .set(CLIENT.EMAIL,
                        coalesce(val(updateClient.getEmail()), CLIENT.EMAIL))
                .set(CLIENT.NAME,
                        coalesce(val(updateClient.getName()), CLIENT.NAME))
                .set(CLIENT.PHONE,
                        coalesce(val(updateClient.getPhone()), CLIENT.PHONE))
                .where(CLIENT.ID.eq(id))
                .returning()
                .fetchOptional()
                .map(r -> r.into(Client.class));
    }

    public void deleteClient(String id) {
        this.dslContext.update(CLIENT)
                .set(CLIENT.DELETED, true)
                .where(CLIENT.ID.eq(UUID.fromString(id)))
                .execute();
    }

    private Optional<Client> get(String entity, TableField<?, ?> tableField) {
        final List<Condition> conditions = List.of(
                getConditionByField(entity, tableField),
                CLIENT.DELETED.eq(false)
        );

        return this.dslContext.selectFrom(CLIENT)
                .where(conditions)
                .fetchOptional()
                .map(r -> r.into(Client.class));
    }

    private Condition getConditionByField(String entity, TableField<?, ?> tableField) {
        if (tableField.equals(CLIENT.ID)) {
            return CLIENT.ID.eq(UUID.fromString(entity));
        } else {
            return CLIENT.EMAIL.eq(entity);
        }
    }




}


