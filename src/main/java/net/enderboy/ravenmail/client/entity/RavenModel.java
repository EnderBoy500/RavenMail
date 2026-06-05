// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package net.enderboy.ravenmail.client.entity;

import net.enderboy.ravenmail.RavenMail;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class RavenModel extends EntityModel<RavenRenderState> {
	public static final ModelLayerLocation RAVEN = new ModelLayerLocation(RavenMail.id("raven"), "main");

	private final ModelPart raven;
	public final ModelPart body;
	private final ModelPart left_wing;
	private final ModelPart right_wing;
	private final ModelPart legs;
	private final ModelPart left;
	private final ModelPart right;

	private final KeyframeAnimation walkingAnimation;
	private final KeyframeAnimation flyingAnimation;

	public RavenModel(ModelPart root) {
        super(root);
        this.raven = root.getChild("raven");
		this.body = this.raven.getChild("body");
		this.left_wing = this.body.getChild("left_wing");
		this.right_wing = this.body.getChild("right_wing");
		this.legs = this.raven.getChild("legs");
		this.left = this.legs.getChild("left");
		this.right = this.legs.getChild("right");

		walkingAnimation = RavenAnimations.walking.bake(root);
		flyingAnimation = RavenAnimations.fly.bake(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition raven = partdefinition.addOrReplaceChild("raven", CubeListBuilder.create(), PartPose.offset(0.1F, 24.0F, 1.0F));

		PartDefinition body = raven.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -5.0F, -5.0F, 5.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(26, 29).addBox(-1.5F, -3.0F, -7.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 13).addBox(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(16, 13).addBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(26, 0).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(26, 7).addBox(0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -3.0F, -2.0F));

		PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 13).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(16, 13).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(26, 0).addBox(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(26, 7).addBox(-1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -3.0F, -2.0F));

		PartDefinition legs = raven.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition left = legs.addOrReplaceChild("left", CubeListBuilder.create().texOffs(32, 15).addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(32, 13).addBox(0.0F, 2.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, 0.0F));

		PartDefinition right = legs.addOrReplaceChild("right", CubeListBuilder.create().texOffs(32, 14).addBox(-1.0F, 2.0F, -1.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(32, 17).addBox(-1.0F, 0.0F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(RavenRenderState object) {
		super.setupAnim(object);
		if (object.isSitting) this.body.setPos(0, -0.5f,0);
		else this.body.resetPose();
		if (object.flying) this.flyingAnimation.applyWalk(object.walkAnimationPos, object.walkAnimationSpeed, 2, 2.5f);
		else this.walkingAnimation.applyWalk(object.walkAnimationPos, object.walkAnimationSpeed, 2, 2.5f);
	}
}