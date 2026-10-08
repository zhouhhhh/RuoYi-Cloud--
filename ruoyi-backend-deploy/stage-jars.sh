#!/usr/bin/env bash
# 只复制已有打包结果，不执行 Maven；先检查所有输入，再复制。
set -euo pipefail
if [ "$#" -lt 1 ] || [ "$#" -gt 2 ]; then
  echo "用法: bash stage-jars.sh 后端项目根目录 [all|gateway|auth|system|gen|job|file|monitor]" >&2
  exit 2
fi
src="$1"
selected="${2:-all}"
base="$(cd "$(dirname "$0")" && pwd)"
case "$selected" in all|gateway|auth|system|gen|job|file|monitor) ;; *) echo "未知服务: $selected" >&2; exit 2;; esac
names=()
files=()
if [ "$selected" = all ] || [ "$selected" = gateway ]; then
  names+=(gateway)
  files+=("$src/ruoyi-gateway/target/ruoyi-gateway.jar")
fi
if [ "$selected" = all ] || [ "$selected" = auth ]; then
  names+=(auth)
  files+=("$src/ruoyi-auth/target/ruoyi-auth.jar")
fi
if [ "$selected" = all ] || [ "$selected" = system ]; then
  names+=(system)
  files+=("$src/ruoyi-modules/ruoyi-system/target/ruoyi-modules-system.jar")
fi
if [ "$selected" = all ] || [ "$selected" = gen ]; then
  names+=(gen)
  files+=("$src/ruoyi-modules/ruoyi-gen/target/ruoyi-modules-gen.jar")
fi
if [ "$selected" = all ] || [ "$selected" = job ]; then
  names+=(job)
  files+=("$src/ruoyi-modules/ruoyi-job/target/ruoyi-modules-job.jar")
fi
if [ "$selected" = all ] || [ "$selected" = file ]; then
  names+=(file)
  files+=("$src/ruoyi-modules/ruoyi-file/target/ruoyi-modules-file.jar")
fi
if [ "$selected" = all ] || [ "$selected" = monitor ]; then
  names+=(monitor)
  files+=("$src/ruoyi-visual/ruoyi-monitor/target/ruoyi-visual-monitor.jar")
fi
for f in "${files[@]}"; do
  [ -s "$f" ] || { echo "缺少 JAR: $f" >&2; exit 1; }
done
for ((i=0; i<${#names[@]}; i++)); do
  cp "${files[$i]}" "$base/build/${names[$i]}/app.jar"
  shasum -a 256 "$base/build/${names[$i]}/app.jar"
done
