package io.siuolplex.fhcoal.mixin;

import com.cake.trading_floor.Config;
import com.cake.trading_floor.content.trading_depot.TradingDepotBlockEntity;
import com.cake.trading_floor.content.trading_depot.behavior.TradingDepotBehaviour;
import com.cake.trading_floor.foundation.MerchantOfferInfo;
import com.cake.trading_floor.foundation.access.VillagerExperienceAccessor;
import com.cake.trading_floor.foundation.advancement.TFAdvancementBehaviour;
import com.cake.trading_floor.foundation.advancement.TFAdvancements;
import com.cake.trading_floor.registry.TFParticleEmitters;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(TradingDepotBlockEntity.class)
public abstract class TradingDepotBlockEntityMixin extends SmartBlockEntity implements IHaveGoggleInformation {

    @Shadow
    TradingDepotBehaviour tradingDepotBehaviour;

    @Shadow
    FilteringBehaviour filtering;

    public TradingDepotBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    private static boolean isRequiredItem(ItemStack available, ItemStack cost) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    protected abstract boolean tryTakeMerchantOffer(MerchantOffer offer, TradingDepotBehaviour costASource, List<TradingDepotBehaviour> costBSources);

    @Shadow
    @Nullable
    MerchantOfferInfo lastTrade;

    @Shadow
    int currentTradeCompletedCount;

    @Shadow
    int tradeOutputSum;

    @Shadow
    int lastTradeCount;

    @Shadow
    protected abstract int getExperienceProduced();

    @Shadow
    protected abstract void checkForAwardedAdvancements();

    /**
     * @author Siuol
     * @reason I dont wanna have to target this mixin harder and I anyone else will want to target this.
     */
    // Holy shit I had a stroke when writing that Im not changing it fuck you
    @Overwrite
    public void tryTradeWith(Villager villager, List<TradingDepotBehaviour> allDepots) {
        if (!tradingDepotBehaviour.isOutputEmpty()) return;

        //Don't use self
        List<TradingDepotBehaviour> costBSources = allDepots.stream()
                .filter(depot -> depot != tradingDepotBehaviour)
                .toList();

        boolean hadSuccessfulTrade = false;
        boolean hasSpace = true;

        MerchantOfferInfo latestTrade = null;
        MerchantOffer latestTradeOffer = null;
        int latestTradeCount = 0;

        List<MerchantOffer> validOffers = new ArrayList<>(villager.getOffers().stream().filter(offer -> (!filtering.getFilter().isEmpty() && (filtering.getFilter().getItem() instanceof FilterItem) ? filtering.test(offer.getResult()) : isRequiredItem(offer.getResult(), filtering.getFilter()))).toList());
        Collections.shuffle(validOffers);

        for (MerchantOffer offer : validOffers) {
            if (!hasSpace) break;

            List<TradingDepotBehaviour> filteredCostBSources = costBSources.stream().filter(depot -> depot.canBeUsedFor(offer)).toList();

            boolean trading = true;
            while (trading) {
                if (tradingDepotBehaviour.getResults().size() >= 8) {
                    tradingDepotBehaviour.combineOutputs();
                    if (tradingDepotBehaviour.getResults().size() >= 8) {
                        hasSpace = false;
                        break;
                    }
                }

                trading = tryTakeMerchantOffer(offer, tradingDepotBehaviour, filteredCostBSources);

                if (trading) {
                    latestTrade = new MerchantOfferInfo(offer);
                    latestTradeOffer = offer;
                    latestTradeCount++;
                }

                hadSuccessfulTrade = hadSuccessfulTrade || trading;

                if (latestTradeCount >= Config.maxTradePerWork) {
                    break;
                }
            }

            //Only do one type of trade per cycle
            if (hadSuccessfulTrade) break;
        }

        if (hadSuccessfulTrade) {
            tradingDepotBehaviour.combineOutputs();
            villager.playCelebrateSound();
            getBehaviour(TFAdvancementBehaviour.TYPE).awardPlayer(TFAdvancements.MONEY_MONEY_MONEY);

            if (level instanceof ServerLevel serverLevel)
                TFParticleEmitters.TRADE_COMPLETED.emitToClients(serverLevel, Vec3.atCenterOf(getBlockPos()).add(0, 0.4, 0), 4);
        }

        if (latestTrade != null && !Objects.equals(lastTrade, latestTrade)) {
            currentTradeCompletedCount = 0;
            tradeOutputSum = 0;
        }
        lastTrade = latestTrade;

        if (latestTrade != null) {
            currentTradeCompletedCount += latestTradeCount;
            tradeOutputSum += latestTradeCount * latestTrade.getResult().getCount();

            if (Config.shouldProduceVillagerExperience) {
                ((VillagerExperienceAccessor) villager).trading_Floor_Neoforge$addExperienceForTrade(latestTradeCount, latestTradeOffer);
            }

            lastTradeCount = latestTradeCount;
        }

        int experienceCount = getExperienceProduced();
        if (experienceCount > 0) {
            tradingDepotBehaviour.getResults().add(new ItemStack(AllItems.EXP_NUGGET.get(), experienceCount));
        }

        checkForAwardedAdvancements();

        notifyUpdate();
    }

}
