package io.github.treesoid.nations.components.ability;

import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.treesoid.nations.abilities.player.PlayerAbilityList;

public interface IAbilityComponent extends Component {
    PlayerAbilityList get();
    void set(PlayerAbilityList abilities);
}
