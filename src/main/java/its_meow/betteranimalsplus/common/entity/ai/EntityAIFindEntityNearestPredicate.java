package its_meow.betteranimalsplus.common.entity.ai;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;

public class EntityAIFindEntityNearestPredicate extends EntityAIBase {
    
    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityLiving mob;
    private final Predicate<EntityLivingBase> predicate;
    private final EntityAINearestAttackableTarget.Sorter sorter;
    private EntityLivingBase target;
    private final Class <? extends EntityLivingBase > classToCheck;

    public EntityAIFindEntityNearestPredicate(EntityLiving mobIn, Class <? extends EntityLivingBase > p_i45884_2_, Predicate<EntityLivingBase> predicate)
    {
        this.mob = mobIn;
        this.classToCheck = p_i45884_2_;

        if (mobIn instanceof EntityCreature)
        {
            LOGGER.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }

        this.predicate = new Predicate<EntityLivingBase>()
        {
            public boolean apply(@Nullable EntityLivingBase p_apply_1_)
            {
                double d0 = EntityAIFindEntityNearestPredicate.this.getFollowRange();

                if (p_apply_1_.isSneaking())
                {
                    d0 *= 0.800000011920929D;
                }

                if (p_apply_1_.isInvisible())
                {
                    return false;
                }
                else
                {
                    return predicate.apply(p_apply_1_) && (double)p_apply_1_.getDistance(EntityAIFindEntityNearestPredicate.this.mob) > d0 ? false : EntityAITarget.isSuitableTarget(EntityAIFindEntityNearestPredicate.this.mob, p_apply_1_, false, true);
                }
            }
        };
        this.sorter = new EntityAINearestAttackableTarget.Sorter(mobIn);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        double d0 = this.getFollowRange();
        List<EntityLivingBase> list = this.mob.world.<EntityLivingBase>getEntitiesWithinAABB(this.classToCheck, this.mob.getBoundingBox().grow(d0, 4.0D, d0), this.predicate);
        Collections.sort(list, this.sorter);

        if (list.isEmpty())
        {
            return false;
        }
        else
        {
            this.target = list.get(0);
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        EntityLivingBase entitylivingbase = this.mob.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isAlive())
        {
            return false;
        }
        else
        {
            double d0 = this.getFollowRange();

            if (this.mob.getDistanceSq(entitylivingbase) > d0 * d0)
            {
                return false;
            }
            else
            {
                return !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).interactionManager.isCreative();
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.mob.setAttackTarget(this.target);
        super.startExecuting();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.mob.setAttackTarget((EntityLivingBase)null);
        super.startExecuting();
    }

    protected double getFollowRange()
    {
        IAttributeInstance iattributeinstance = this.mob.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getValue();
    }

}