uniform sampler2D RTScene; // the texture with the scene you want to blur
varying vec2 vTexCoord;
 
const float blurSize = 1/512.0; // I've chosen this size because this will result in that every step will be one pixel wide if the RTScene texture is of size 512x512
 
void main(void)
{
   vec4 sum = vec4(0.0);
 
   // blur in x (horizontal)
   // take nine samples, with the distance blurSize between them
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x - 4.0*blurSize, vTexCoord.y)) * 0.05;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x - 3.0*blurSize, vTexCoord.y)) * 0.09;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x - 2.0*blurSize, vTexCoord.y)) * 0.12;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x - blurSize, vTexCoord.y)) * 0.15;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x, vTexCoord.y)) * 0.16;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x + blurSize, vTexCoord.y)) * 0.15;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x + 2.0*blurSize, vTexCoord.y)) * 0.12;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x + 3.0*blurSize, vTexCoord.y)) * 0.09;
   sum += texture2D(RTScene, gl_TexCoord[0].st + vec2(vTexCoord.x + 4.0*blurSize, vTexCoord.y)) * 0.05;
 
   gl_FragColor = sum;
}