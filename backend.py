from starlette.responses import FileResponse 

from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles

app = FastAPI()

# Serve static webpage files

@app.get("/")
async def read_index():
    return FileResponse('frontend/index.html')

@app.get("/globals.css")
async def read_index():
    return FileResponse('frontend/globals.css')

@app.get("/static/main.js")
async def read_index():
    return FileResponse('frontend/static/main.js')

