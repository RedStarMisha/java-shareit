package ru.practicum.shareit.booking.strategy.forbooker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.strategy.BookingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyForBookerFactory {

    private Map<BookingState, StrategyForBooker> strategies;

    @Autowired
    public StrategyForBookerFactory(Set<StrategyForBooker> strategyForBookerSet) {
        createStrategy(strategyForBookerSet);
    }

    public StrategyForBooker findStrategy(BookingState state) {
        return strategies.get(state);
    }

    private void createStrategy(Set<StrategyForBooker> strategyForBookerSet) {
        strategies = new HashMap<>();
        strategyForBookerSet.forEach(strategy -> strategies.put(strategy.getState(), strategy));
    }

}
