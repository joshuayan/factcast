/*
 * Copyright © 2018 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.factcast.core.lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.UUID;

import org.factcast.core.lock.LockedOperationBuilder.OnBuilderStep;
import org.factcast.core.store.FactStore;
import org.junit.jupiter.api.Test;

public class LockedOperationBuilderTest {

    LockedOperationBuilder uut = new LockedOperationBuilder(mock(FactStore.class), "ns");

    @Test
    public void testOn() {

        UUID id = UUID.randomUUID();
        OnBuilderStep on = uut.on(id);
        assertThat(on.ids()).hasSize(1).contains(id);

        UUID id2 = UUID.randomUUID();
        on = uut.on(id, id2);
        assertThat(on.ids()).hasSize(2).contains(id).contains(id2);

        assertThrows(NullPointerException.class, () -> {
            uut.on(null);
        });
    }

    @Test
    public void testOptimistic() throws Exception {
        UUID id = new UUID(1, 2);
        WithOptimisticLock wol = uut.on(id).optimistic();
        assertThat(wol).isNotNull();
        assertThat(wol.ns()).isEqualTo("ns");
        assertThat(wol.ids().get(0)).isEqualTo(id);
        assertThat(wol.ids().size()).isEqualTo(1);
    }

}