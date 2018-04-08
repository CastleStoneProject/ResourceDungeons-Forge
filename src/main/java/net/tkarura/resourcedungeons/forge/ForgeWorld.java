package net.tkarura.resourcedungeons.forge;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.tkarura.resourcedungeons.core.server.IDungeonWorld;
import net.tkarura.resourcedungeons.core.util.nbt.DNBTTagCompound;

public class ForgeWorld implements IDungeonWorld {

	private World world;

	public ForgeWorld(World world) {
		this.world = world;
	}

	@Override
	public void spawnEntityFromId(String entity_id, int x, int y, int z) {
		ResourceLocation rs = new ResourceLocation(entity_id);
		Entity entity = EntityList.createEntityByIDFromName(rs, world);
		entity.posX = x;
		entity.posY = y;
		entity.posZ = z;
		this.world.spawnEntity(entity);
	}

	@Override
	public void spawnEntityFromId(int id, int x, int y, int z) {
		Entity entity = EntityList.createEntityByID(id, world);
		entity.posX = x;
		entity.posY = y;
		entity.posZ = z;
		this.world.spawnEntity(entity);
	}

	@Override
	public void spawnEntityFromNBT(DNBTTagCompound nbt, int x, int y, int z) {
		NBTTagCompound mnbt = NBTTagConverter.convert(nbt);
		Entity entity = EntityList.createEntityFromNBT(mnbt, world);
		entity.posX = x;
		entity.posY = y;
		entity.posZ = z;
		this.world.spawnEntity(entity);
	}

	@Override
	public void setBlockId(String registry_id, int x, int y, int z) {
		String[] name = registry_id.split(":");
		String domain = name.length > 1 ? name[0] : "minecraft";
		String path = name.length > 1 ? name[1] : registry_id;
		ResourceLocation resource = new ResourceLocation(domain, path);
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = Block.REGISTRY.getObject(resource).getDefaultState();
		this.setBlock(pos, state);
	}

	@Override
	public void setBlockId(int block_id, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		Block block = Block.getBlockById(block_id);
		this.setBlock(pos, block.getDefaultState());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBlockData(byte data, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		Block block = this.world.getBlockState(pos).getBlock();
		IBlockState state = block.getStateFromMeta(data);
		this.setBlock(pos, state);
	}

	@Override
	public void setTileEntityFromNBT(DNBTTagCompound nbt, int x, int y, int z) {
		NBTTagCompound mnbt = NBTTagConverter.convert(nbt);
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = TileEntity.create(this.world, mnbt);
		this.world.setTileEntity(pos, tile);
	}

	@Override
	public void setBiome(String biome_name, int x, int z) {
		String name[] = biome_name.split(":");
		String domain = name.length > 1 ? name[0] : "minecraft";
		String path = name.length > 1 ? name[1] : biome_name;
		ResourceLocation resource = new ResourceLocation(domain, path);
		Biome biome = Biome.REGISTRY.getObject(resource);
		Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, 0, z));
		byte biome_array[] = chunk.getBiomeArray();
		biome_array[(z & 0xF) << 4 | (x & 0xF)] = (byte) Biome.getIdForBiome(biome);
		chunk.setBiomeArray(biome_array);
	}

	@Override
	public String getBlockId(int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = this.world.getBlockState(pos);
		ResourceLocation loc = Block.REGISTRY.getNameForObject(state.getBlock());
		return loc.getResourceDomain() + ":" + loc.getResourcePath();
	}

	@Override
	public byte getBlockData(int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		IBlockState state = this.world.getBlockState(pos);
		return (byte) state.getBlock().getMetaFromState(state);
	}

	@Override
	public String getBiome(int x, int z) {
		Biome biome = world.getBiome(new BlockPos(x, 0, z));
		ResourceLocation resource = Biome.REGISTRY.getNameForObject(biome);
		return resource.getResourceDomain() + ":" + resource.getResourcePath();
	}

	@Override
	public long getSeed() {
		return this.world.getSeed();
	}

	private void setBlock(BlockPos pos, IBlockState state) {
		this.world.setBlockState(pos, state, 2);
	}

}
