/*
 *     KookBC -- The Kook Bot Client & JKook API standard implementation for Java.
 *     Copyright (C) 2022 - 2023 KookBC contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package snw.kookbc.util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLinkedBlockingQueue<E> extends ConcurrentLinkedQueue<E> {
    protected final Lock signalLock = new ReentrantLock();
    protected final Condition notEmptyCondition = signalLock.newCondition();

    @Override
    public boolean offer(E e) {
        super.offer(e);
        signalLock.lock();
        try {
            notEmptyCondition.signalAll();
        } finally {
            signalLock.unlock();
        }
        return true;
    }

    @NotNull
    public E take() throws InterruptedException {
        if (isEmpty()) {
            signalLock.lock();
            try {
                notEmptyCondition.await();
            } finally {
                signalLock.unlock();
            }
        }
        return Objects.requireNonNull(poll());
    }

}
