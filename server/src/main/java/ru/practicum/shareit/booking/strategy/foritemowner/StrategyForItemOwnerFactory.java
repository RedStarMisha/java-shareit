package ru.practicum.shareit.booking.strategy.foritemowner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.strategy.BookingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyForItemOwnerFactory {

    private Map<BookingState, StrategyForItemOwner> strategies;

    @Autowired
    public StrategyForItemOwnerFactory(Set<StrategyForItemOwner> strategy) {
        createStrategy(strategy);
    }

    public StrategyForItemOwner findStrategy(BookingState state) {
        return strategies.get(state);
    }

    private void createStrategy(Set<StrategyForItemOwner> strategySet) {
        strategies = new HashMap<>();
        strategySet.forEach(strategy -> strategies.put(strategy.getState(), strategy));
    }

}
